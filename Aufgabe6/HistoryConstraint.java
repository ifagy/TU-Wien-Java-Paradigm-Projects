import java.lang.annotation.*;

@Author("Efe")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HistoryConstraint {
    String value() default "No History Constraint";
}