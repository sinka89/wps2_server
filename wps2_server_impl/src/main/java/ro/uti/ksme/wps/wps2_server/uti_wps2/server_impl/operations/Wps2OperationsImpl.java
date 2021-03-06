package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.operations;

import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.uti.ksme.wps.common.utils.enums.JobControlOps;
import ro.uti.ksme.wps.common.utils.enums.ProcessState;
import ro.uti.ksme.wps.common.utils.enums.ResponseType;
import ro.uti.ksme.wps.wps2.custom_pojo_types.RawData;
import ro.uti.ksme.wps.wps2.pojo.ows._2.*;
import ro.uti.ksme.wps.wps2.pojo.wps._2.*;
import ro.uti.ksme.wps.wps2.pojo.wps._2.GetCapabilitiesType;
import ro.uti.ksme.wps.wps2.pojo.wps._2.ReferenceType;
import ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.ProcessValidatorService;
import ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.ProcessorService;
import ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.ProcessorServiceSync;
import ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process.*;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.Wps2ServerProps;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.Wps2ServerUtils;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.CacheManagerWpsImpl;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.ProcessExecutionHelper;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.ProcessResultWrapper;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;
import java.net.URI;
import java.util.*;

/**
 * @author Bogdan-Adrian Sincu
 * Class used to execute specific operations based on the type received from the wps server impl
 */
//@Startup
//@Singleton
//@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Service
public class Wps2OperationsImpl implements Wps2Operations {
    private static final Logger LOGGER = LoggerFactory.getLogger(Wps2OperationsImpl.class);

    private CacheManagerWpsImpl cacheManager;
    private Wps2ServerProps wps2ServerProps;
    private ProcessManager processManager;
    private ProcessorServiceSync processorServiceSync;
    private ProcessorService processorService;
    private ProcessValidatorService processValidatorService;

    @Autowired
    public void setCacheManager(CacheManagerWpsImpl cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Autowired
    public void setWps2ServerProps(Wps2ServerProps wps2ServerProps) {
        this.wps2ServerProps = wps2ServerProps;
    }

    @Autowired
    public void setProcessManager(ProcessManager processManager) {
        this.processManager = processManager;
    }

    @Autowired
    public void setProcessorServiceSync(ProcessorServiceSync processorServiceSync) {
        this.processorServiceSync = processorServiceSync;
    }

    @Autowired
    public void setProcessorService(ProcessorService processorService) {
        this.processorService = processorService;
    }

    @Autowired
    public void setProcessValidatorService(ProcessValidatorService processValidatorService) {
        this.processValidatorService = processValidatorService;
    }

    @Override
    public Object getCapabilities(GetCapabilitiesType getCapabilities) {
        //build Local ExceptionReport to reuse and return if something wrong;
        ExceptionReport exceptionReport = new ExceptionReport();
        if (getCapabilities == null) {
            ExceptionType exceptionType = new ExceptionType();
            exceptionType.setExceptionCode("NoApplicableCode");
            exceptionReport.getException().add(exceptionType);
            return exceptionReport;
        }
        //check for version if supported by server
        if (getCapabilities.getAcceptVersions() != null && getCapabilities.getAcceptVersions().getVersion() != null) {
            boolean isVerAccepted = false;
            for (String v : getCapabilities.getAcceptVersions().getVersion()) {
                for (String v2 : wps2ServerProps.SERVER_GLOBAL_PROPERTIES.SUPPORTED_VERSIONS) {
                    if (v.equals(v2)) {
                        isVerAccepted = true;
                    }
                }
            }
            if (!isVerAccepted) {
                ExceptionType exceptionType = new ExceptionType();
                exceptionType.setExceptionCode("VersionNotAccepted");
                exceptionReport.getException().add(exceptionType);
                return exceptionReport;
            }
        }
        //build sections based on standard xsd
        List<SectionType> secReq = new ArrayList<>();
        if (getCapabilities.getSections() != null && getCapabilities.getSections().getSection() != null) {
            for (String sec : getCapabilities.getSections().getSection()) {
                boolean valid = false;
                for (SectionType s : SectionType.values()) {
                    if (sec.equals(s.name())) {
                        valid = true;
                    }
                }
                if (!valid) {
                    ExceptionType exceptionType = new ExceptionType();
                    exceptionType.setExceptionCode("InvalidParameterValue");
                    exceptionType.setLocator("Sections:" + sec);
                    exceptionReport.getException().add(exceptionType);
                    return exceptionReport;
                } else {
                    secReq.add(SectionType.valueOf(sec));
                }
            }
        } else {
            secReq.add(SectionType.All);
        }

        //TODO: use requested lang for process identifier


        //build the WPS Capabilities response
        WPSCapabilitiesType capType = new WPSCapabilitiesType();
        //populate with generic info
        capType.setExtension(new WPSCapabilitiesType.Extension());
        capType.setUpdateSequence(wps2ServerProps.SERVER_GLOBAL_PROPERTIES.SERVER_VERSION);
        capType.setVersion(wps2ServerProps.SERVER_GLOBAL_PROPERTIES.SERVER_VERSION);
        if (secReq.contains(SectionType.All) || secReq.contains(SectionType.Languages)) {
            CapabilitiesBaseType.Languages languages = new CapabilitiesBaseType.Languages();
            for (String language : wps2ServerProps.SERVER_GLOBAL_PROPERTIES.SUPPORTED_LANGUAGES) {
                languages.getLanguage().add(language);
            }
            capType.setLanguages(languages);
        }
        //add supported operationalMetadata
        if (secReq.contains(SectionType.All) || secReq.contains(SectionType.OperationMetadata)) {
            OperationsMetadata opMeta = new OperationsMetadata();
            List<Operation> opList = new ArrayList<>();
            opList.add(wps2ServerProps.OPERATIONS_METADATA_PROPS.GET_CAPABILITIES_OPERATION);
            opList.add(wps2ServerProps.OPERATIONS_METADATA_PROPS.DESCRIBE_PROCESS_OPERATION);
            opList.add(wps2ServerProps.OPERATIONS_METADATA_PROPS.EXECUTE_OPERATION);
            opList.add(wps2ServerProps.OPERATIONS_METADATA_PROPS.GET_RESULT_OPERATION);
            opList.add(wps2ServerProps.OPERATIONS_METADATA_PROPS.GET_STATUS_OPERATION);
            opList.add(wps2ServerProps.OPERATIONS_METADATA_PROPS.DISMISS_OPERATION);
            opMeta.getOperation().addAll(opList);
            capType.setOperationsMetadata(opMeta);
        }
        //populate serviceIdentification withing server
        if (secReq.contains(SectionType.All) || secReq.contains(SectionType.ServiceIdentification)) {
            ServiceIdentification serviceIdentification = new ServiceIdentification();
            serviceIdentification.setFees(wps2ServerProps.SERVER_IDENTIFICATION_PROPS.FEES);
            serviceIdentification.setServiceType(wps2ServerProps.SERVER_IDENTIFICATION_PROPS.SERVICE_TYPE);
            for (String version : wps2ServerProps.SERVER_IDENTIFICATION_PROPS.SERVICE_TYPE_VERSIONS) {
                serviceIdentification.getServiceTypeVersion().add(version);
            }
            for (LanguageStringType title : wps2ServerProps.SERVER_IDENTIFICATION_PROPS.TITLE) {
                serviceIdentification.getTitle().add(title);
            }
            capType.setServiceIdentification(serviceIdentification);
        }
        if (secReq.contains(SectionType.All) || secReq.contains(SectionType.ServiceProvider)) {
            ServiceProvider serviceProvider = new ServiceProvider();
            serviceProvider.setProviderName(wps2ServerProps.SERVICE_PROVIDER_PROPS.PROVIDER_NAME);
            serviceProvider.setProviderSite(wps2ServerProps.SERVICE_PROVIDER_PROPS.PROVIDER_SITE);
        }

        if (secReq.contains(SectionType.All) || secReq.contains(SectionType.Contents)) {
            Contents contents = new Contents();
            List<ProcessSummaryType> processSummaryTypeList = new ArrayList<>();
            List<ProcessIdentifier> processIdList = processManager.getAllProcessIdentifier();
            for (ProcessIdentifier pId : processIdList) {
                ProcessDescriptionType process = pId.getProcessDescriptionType();
                //todo: ??? translate process to more readable form / language?
                ProcessSummaryType processSummaryType = new ProcessSummaryType();
                processSummaryType.getJobControlOptions().addAll(Arrays.asList(wps2ServerProps.SERVER_GLOBAL_PROPERTIES.PROCESS_CONTROL_OPTIONS));
                processSummaryType.setIdentifier(process.getIdentifier());
                processSummaryType.getMetadata().addAll(process.getMetadata());
                processSummaryType.getAbstract().addAll(process.getAbstract());
                processSummaryType.getTitle().addAll(process.getTitle());
                processSummaryType.getKeywords().addAll(process.getKeywords());
                processSummaryType.setProcessVersion(pId.getProcessOffering().getProcessVersion());
                processSummaryType.setProcessModel(pId.getProcessOffering().getProcessModel());
                processSummaryType.getOutputTransmission().addAll(pId.getProcessOffering().getOutputTransmission());

                processSummaryTypeList.add(processSummaryType);
            }
            processSummaryTypeList.sort((o1, o2) -> o1.getIdentifier().getValue().compareToIgnoreCase(o2.getIdentifier().getValue()));
            contents.getProcessSummary().addAll(processSummaryTypeList);
            capType.setContents(contents);
        }
        return capType;
    }

    @Override
    public Object describeProcess(DescribeProcess describeProcess) {
        List<CodeType> idList = describeProcess.getIdentifier();

        ProcessOfferings processOfferings = new ProcessOfferings();
        List<ProcessOffering> processOfferingList = new ArrayList<>();
        for (CodeType id : idList) {
            List<ProcessIdentifier> piList = processManager.getAllProcessIdentifier();

            for (ProcessIdentifier pi : piList) {
                if (pi.getProcessDescriptionType().getIdentifier().getValue().equals(id.getValue())) {
                    if (pi.getProcessOffering() != null) {
                        ProcessOffering processOffering = new ProcessOffering();
                        processOffering.setProcessVersion(pi.getProcessOffering().getProcessVersion());
                        //treat job control used on process if not specified use server defaults
                        if (pi.getProcessOffering().getJobControlOptions().size() > 0) {
                            processOffering.getJobControlOptions().addAll(pi.getProcessOffering().getJobControlOptions());
                        } else {
                            processOffering.getJobControlOptions().addAll(Arrays.asList(wps2ServerProps.SERVER_GLOBAL_PROPERTIES.PROCESS_CONTROL_OPTIONS));
                        }
                        processOffering.getOutputTransmission().addAll(pi.getProcessOffering().getOutputTransmission());
                        processOffering.setProcess(pi.getProcessDescriptionType());
                        processOfferingList.add(processOffering);
                    }
                }
            }
        }
        if (processOfferingList.isEmpty()) {
            ExceptionType exceptionType = new ExceptionType();
            exceptionType.setExceptionCode("NoSuchProcess");
            exceptionType.getExceptionText().add("One of the identifiers passed does not match with any of the process offered by the server.");
            ExceptionReport exceptionReport = new ExceptionReport();
            exceptionReport.getException().add(exceptionType);
            return exceptionReport;
        } else {
            processOfferings.getProcessOffering().addAll(processOfferingList);
            return processOfferings;
        }
    }

    @Override
    public Object execute(ExecuteRequestType execute) throws Exception {
        ProcessIdentifier processIdentifier = processManager.getProcessIdentifier(execute.getIdentifier());
        processValidatorService.validateInputForExecutionProcess(processIdentifier, execute.getInput());
        processValidatorService.validateOutputForExecutionProcess(processIdentifier, execute.getOutput());
        Object toReturn = null;
        Map<URI, Object> dataMap = new HashMap<>();
        for (DataInputType input : execute.getInput()) {
            URI id = URI.create(input.getId());
            Object data;
            if (input.getData().getContent().size() == 1) {
                data = input.getData().getContent().get(0);
            } else if (input.getData().getContent().size() == 0) {
                data = null;
            } else {
                data = input.getData().getContent();
            }
            dataMap.put(id, data);
        }

        UUID jobId = UUID.randomUUID();

        if (execute.getMode() != null && processIdentifier.getProcessOffering().getJobControlOptions().contains(JobControlOps.getEnumByJobMode(execute.getMode()).getJobControlMode()) &&
                JobControlOps.getEnumByJobMode(execute.getMode()).equals(JobControlOps.SYNC)) {
            ProcessJobSync job = new ProcessJobSync(processIdentifier.getProcessDescriptionType(), jobId, dataMap);
            ProcessJobSync processJobSync = processorServiceSync.executeProcessSync(job, processIdentifier, dataMap);
            if (execute.getResponse().equalsIgnoreCase(ResponseType.DOCUMENT.getType())) {
                toReturn = getResult(processJobSync);
            } else if (execute.getResponse().equalsIgnoreCase(ResponseType.RAW.getType())) {
                toReturn = getRawResult(processJobSync);
            }
        } else {
            ProcessJob job = new ProcessJob(processIdentifier.getProcessDescriptionType(), jobId, dataMap, wps2ServerProps.CUSTOM_PROPS.MAX_PROCESS_POOLING_DELAY, wps2ServerProps.CUSTOM_PROPS.BASE_PROCESS_POOLING_DELAY);
            if (execute.getResponse().equalsIgnoreCase(ResponseType.RAW.getType())) {
                job.setReturnRawData(true);
            }
            cacheManager.getJobProcessCache().put(new Element(jobId, job));
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setJobID(jobId.toString());
            statusInfo.setStatus(job.getState().name());
            processorService.executeNewProcessWorker(job, processIdentifier, dataMap);
            statusInfo.setStatus(job.getState().name());
            XMLGregorianCalendar date = Wps2ServerUtils.getXMLGregorianCalendarWithDelay(job.getProcessPoolingTime());
            statusInfo.setNextPoll(date);
            toReturn = statusInfo;
        }
        return toReturn;
    }

    @Override
    public Object getStatus(GetStatus getStatus) {
        UUID jobId = UUID.fromString(getStatus.getJobID());
        Element element = cacheManager.getJobProcessCache().get(jobId);

        if (element == null) {
            ExceptionReport exceptionReport = new ExceptionReport();
            ExceptionType exceptionType = new ExceptionType();
            exceptionType.setExceptionCode("NoSuchProcessActive");
            exceptionType.getExceptionText().add("Cannot identify the process with the UUID: " + jobId);
            exceptionReport.getException().add(exceptionType);
            return exceptionReport;
        }
        ProcessJob job = (ProcessJob) element.getObjectValue();
        StatusInfo statusInfo = new StatusInfo();
        statusInfo.setJobID(jobId.toString());
        statusInfo.setStatus(job.getState().name());
        int progress = job.getProgress();
        statusInfo.setPercentCompleted(progress);
        if (progress != 0 && progress != 100) {
            long milliSpent = System.currentTimeMillis() - job.getStartTime();
            long milliLeft = (milliSpent / progress) * (100 - progress);
            statusInfo.setEstimatedCompletion(Wps2ServerUtils.getXMLGregorianCalendarWithDelay(milliLeft));
        }
        if (!job.getState().equals(ProcessState.FAILED) &&
                !job.getState().equals(ProcessState.FINISHED)) {
            XMLGregorianCalendar date = Wps2ServerUtils.getXMLGregorianCalendarWithDelay(job.getProcessPoolingTime());
            statusInfo.setNextPoll(date);
        }
        return statusInfo;
    }

    @Override
    public Object getResult(GetResult getResult) {
        UUID jobId = UUID.fromString(getResult.getJobID());
        Element element = cacheManager.getJobProcessCache().get(jobId);
        ExceptionReport exceptionReport = new ExceptionReport();
        ExceptionType exceptionType = new ExceptionType();
        if (element == null) {
            exceptionType.setExceptionCode("NoSuchProcessResult");
            exceptionType.getExceptionText().add("Cannot identify the process with the UUID: " + jobId);
            exceptionReport.getException().add(exceptionType);
            return exceptionReport;
        }

        AbstractProcessJob job = (AbstractProcessJob) element.getObjectValue();
        if (!((ProcessJob) job).getState().equals(ProcessState.FINISHED)) {
            exceptionType.setExceptionCode("NoSuchProcessResult");
            exceptionType.getExceptionText().add("Process with UUID: " + jobId + " has not completed or completed with error... use getStatus to check the current state before trying to get the result");
            exceptionReport.getException().add(exceptionType);
            return exceptionReport;
        }
        if (job.isReturnRawData()) {
            return getRawResult(job);
        }
        Result result = new Result();
        result.setExpirationDate(Wps2ServerUtils.getXMLGregorianCalendar(element.getExpirationTime()));
        result.setJobID(jobId.toString());
        List<DataOutputType> listO = new ArrayList<>();
        processOutputDataForProcess(job, listO);
        result.getOutput().addAll(listO);
        return result;
    }

    private Object getRawResult(AbstractProcessJob job) {
        ProcessIdentifier pi = processManager.getProcessIdentifier(job.getProcess().getIdentifier());
        boolean isOutput = false;
        for (Map.Entry<URI, Object> entry : job.getDataMap().entrySet()) {
            for (OutputDescriptionType o : job.getProcess().getOutput()) {
                if (o.getIdentifier().getValue().equals(entry.getKey().toString())) {
                    isOutput = true;
                    break;
                }
            }
            if (isOutput && pi.getProcessOffering().getOutputTransmission().contains(DataTransmissionModeType.VALUE)) {
                Object obj;
                if (entry.getValue() instanceof RawData) {
                    obj = ((RawData) entry.getValue()).getByteData();
                } else {
                    obj = entry.getValue();
                }
                return obj;
            }
            isOutput = false;
        }
        return null;
    }

    private Result getResult(AbstractProcessJob job) {
        Result result = new Result();
        result.setExpirationDate(Wps2ServerUtils.getXMLGregorianCalendar(System.currentTimeMillis()));
        UUID jobId = job.getId();
        result.setJobID(jobId.toString());
        List<DataOutputType> listO = new ArrayList<>();
        processOutputDataForProcess(job, listO);
        result.getOutput().addAll(listO);
        return result;
    }

    private void processOutputDataForProcess(AbstractProcessJob job, List<DataOutputType> listO) {
        ProcessIdentifier pi = processManager.getProcessIdentifier(job.getProcess().getIdentifier());
        for (Map.Entry<URI, Object> entry : job.getDataMap().entrySet()) {
            boolean isOutput = false;
            OutputDescriptionType outputDescriptionType = null;
            for (OutputDescriptionType o : job.getProcess().getOutput()) {
                if (o.getIdentifier().getValue().equals(entry.getKey().toString())) {
                    isOutput = true;
                    outputDescriptionType = o;
                }
            }
            if (isOutput) {
                List<DataTransmissionModeType> outputTransmission = pi.getProcessOffering().getOutputTransmission();
                DataOutputType outputType = new DataOutputType();
                for (DataTransmissionModeType t : outputTransmission) {
                    if (t.equals(DataTransmissionModeType.VALUE)) {
                        outputType.setId(entry.getKey().toString());
                        Data data = new Data();
                        if (outputDescriptionType.getDataDescription() != null && outputDescriptionType.getDataDescription().getValue() != null
                                && outputDescriptionType.getDataDescription().getValue().getFormat().size() > 0) {
                            Format format = outputDescriptionType.getDataDescription().getValue().getFormat().get(0);
                            data.setEncoding(format.getEncoding());
                            data.setMimeType(format.getMimeType());
                        }
                        List<Serializable> list = new ArrayList<>();
                        if (entry.getValue() == null) {
                            list.add("");
                        } else {
                            String result;
                            //testing for ProcessResultWrapper wrapper if not found just take what is and call toString();
                            if (entry.getValue() instanceof ProcessResultWrapper) {
                                ProcessResultWrapper processResultWrapper = (ProcessResultWrapper) entry.getValue();
                                if (processResultWrapper.getData() instanceof byte[]) {
                                    result = Base64.getEncoder().encodeToString((byte[]) processResultWrapper.getData());
                                } else if (processResultWrapper.getData() instanceof RawData) {
                                    result = ProcessExecutionHelper.marshallRawDataTypeResult(processResultWrapper.getData()).toString();
                                } else if (processResultWrapper.getData() instanceof BoundingBoxType) {
                                    result = ProcessExecutionHelper.marshallBoundingBoxTypeResult(processResultWrapper.getData()).toString();
                                } else {
                                    result = processResultWrapper.getData().toString();
                                }
                            } else {
                                result = entry.getValue().toString();
                            }
                            list.add(result);
                        }
                        data.getContent().addAll(list);
                        outputType.setData(data);
                    } else if (t.equals(DataTransmissionModeType.REFERENCE)) {
                        outputType.setId(entry.getKey().toString());
                        //to decide if reference type mimeType get from declaration or let Process override from wrapper
                        ReferenceType refType = new ReferenceType();
                        if (outputDescriptionType.getDataDescription() != null && outputDescriptionType.getDataDescription().getValue() != null
                                && outputDescriptionType.getDataDescription().getValue().getFormat().size() > 0) {
                            Format format = outputDescriptionType.getDataDescription().getValue().getFormat().get(0);
                            refType.setEncoding(format.getEncoding());
                            refType.setMimeType(format.getMimeType());
                        }
                        refType.setHref(((ProcessResultWrapper) entry.getValue()).getData().toString());
                        outputType.setReference(refType);
                    }
                }
                listO.add(outputType);
            }
        }
    }

    @Override
    public Object dismiss(Dismiss dismiss) {
        UUID jobId = UUID.fromString(dismiss.getJobID());
        processorService.cancelProgress(jobId);
        Element element = cacheManager.getJobProcessCache().get(jobId);
        if (element == null) {
            ExceptionReport exceptionReport = new ExceptionReport();
            ExceptionType exceptionType = new ExceptionType();
            exceptionType.setExceptionCode("NoSuchProcessResult");
            exceptionType.getExceptionText().add("Cannot identify the process with the UUID: " + jobId);
            exceptionReport.getException().add(exceptionType);
            return exceptionReport;
        }
        ProcessJob processJob = (ProcessJob) element.getObjectValue();
        StatusInfo statusInfo = new StatusInfo();
        statusInfo.setJobID(jobId.toString());
        statusInfo.setStatus(processJob.getState().name());
        if (!processJob.getState().equals(ProcessState.FAILED) &&
                !processJob.getState().equals(ProcessState.FINISHED)) {
            XMLGregorianCalendar date = Wps2ServerUtils.getXMLGregorianCalendarWithDelay(processJob.getProcessPoolingTime());
            statusInfo.setNextPoll(date);
        }
        return statusInfo;
    }

    private enum SectionType {ServiceIdentification, ServiceProvider, OperationMetadata, Contents, Languages, All}
}
