package ro.uti.ksme.wps.wps2_server.uti_wps2.utils.model;

import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Types;

public enum DataType {
    NUMBER("number"),
    INTEGER("http://www.w3.org/2001/XMLSchema#integer"),
    DOUBLE("http://www.w3.org/2001/XMLSchema#double"),
    FLOAT("http://www.w3.org/2001/XMLSchema#float"),
    SHORT("http://www.w3.org/2001/XMLSchema#short"),
    BYTE("http://www.w3.org/2001/XMLSchema#byte"),
    UNSIGNED_BYTE("http://www.w3.org/2001/XMLSchema#unsignedByte"),
    LONG("http://www.w3.org/2001/XMLSchema#long"),
    STRING("http://www.w3.org/2001/XMLSchema#string"),
    BOOLEAN("http://www.w3.org/2001/XMLSchema#boolean"),

    OTHER("OTHER"),
    GEOMETRY("GEOMETRY"),
    POINT("POINT"),
    LINESTRING("LINESTRING"),
    POLYGON("POLYGON"),
    MULTIPOINT("MULTIPOINT"),
    MULTILINESTRING("MULTILINESTRING"),
    MULTIPOLYGON("MULTIPOLYGON"),
    GEOMCOLLECTION("GEOMCOLLECTION"),
    NONE("NONE");

    private URI uri;

    DataType(String uriString) {
        try {
            this.uri = new URI(uriString);
        } catch (URISyntaxException e) {
            LoggerFactory.getLogger(DataType.class).error(e.getMessage());
        }
    }


    public static DataType getDataType(int sqlTypeId) {
        switch (sqlTypeId) {
            case Types.BOOLEAN:
            case Types.BIT:
                return BOOLEAN;
            case Types.DOUBLE:
                return DOUBLE;
            case Types.NUMERIC:
                return NUMBER;
            case Types.DECIMAL:
            case Types.REAL:
            case Types.FLOAT:
                return FLOAT;
            case Types.BIGINT:
            case Types.INTEGER:
                return INTEGER;
            case Types.SMALLINT:
                return SHORT;
            case Types.VARCHAR:
            case Types.NCHAR:
            case Types.CHAR:
                return STRING;
            default:
                return OTHER;
        }
    }

    public URI getUri() {
        return uri;
    }
}
