package netology.javadiplomafilesupload.log;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LogMarker {

    public static final Marker AUTH = MarkerFactory.getMarker("AUTH");
    public static final Marker READ = MarkerFactory.getMarker("READ");
    public static final Marker CREATE = MarkerFactory.getMarker("CREATE");
    public static final Marker UPDATE = MarkerFactory.getMarker("UPDATE");
    public static final Marker DELETE = MarkerFactory.getMarker("DELETE");
}
