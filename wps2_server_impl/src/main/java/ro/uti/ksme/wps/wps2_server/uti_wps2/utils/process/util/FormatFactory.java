package ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util;

import ro.uti.ksme.wps.wps2.pojo.wps._2.Format;

import java.util.ArrayList;
import java.util.List;

//create formats
public class FormatFactory {
    public static final String TEXT_EXTENSION = ".txt";
    public static final String TEXT_MIMETYPE = "text/plain";
    public static final String OTHER_URI = "";

    public static final String XML_EXTENSION = ".xml";
    public static final String XML_MIMETYPE = "text/xml";
    public static final String DEFAULT_ENCODING = "UTF-8";

    public static final String GML_EXTENSION = "gml";
    public static final String GML_MIMETYPE = "text/xml";
    public static final String GML_URI = "http://schemas.opengis.net/gml/3.2.1/feature.xsd";

    public static final String JSON_EXTENSION = ".json";
    public static final String JSON_MIMETYPE = "application/json";

    public static final String PNG_EXTENSION = ".png";
    public static final String PNG_MIMETYPE = "image/png";

    public static final String JPEG_EXTENSION = ".jpeg";
    public static final String JPEG_MIMETYPE = "image/jpeg";

    public static final String TIFF_EXTENSION = ".tiff";
    public static final String TIFF_MIMETYPE = "image/tiff";

    public static Format getFormatFromExtension(String extension) {
        Format format = new Format();
        switch (extension) {
            case XML_EXTENSION:
                format.setMimeType(XML_MIMETYPE);
                format.setEncoding(DEFAULT_ENCODING);
                break;
            case JSON_EXTENSION:
                format.setEncoding(DEFAULT_ENCODING);
                format.setMimeType(JSON_MIMETYPE);
                break;
            case GML_EXTENSION:
                format.setMimeType(GML_MIMETYPE);
                format.setEncoding(DEFAULT_ENCODING);
                format.setSchema(GML_URI);
                break;
            case PNG_EXTENSION:
                format.setMimeType(PNG_MIMETYPE);
                break;
            case JPEG_EXTENSION:
                format.setMimeType(JPEG_MIMETYPE);
                break;
            case TIFF_EXTENSION:
                format.setMimeType(TIFF_MIMETYPE);
                break;
            default:
                format.setMimeType(TEXT_MIMETYPE);
                break;
        }
        return format;
    }

    public static List<Format> getFormatFromExtensions(String... extensions) {
        List<Format> formats = new ArrayList<>();
        for (String extension : extensions) {
            formats.add(getFormatFromExtension(extension));
        }
        return formats;
    }
}
