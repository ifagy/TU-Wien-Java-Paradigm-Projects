import java.lang.annotation.*;

@Author("Efe")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Invariant {
    String value() default "No Invariant";
}
