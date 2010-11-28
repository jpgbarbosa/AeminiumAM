package unused;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface VarType {
	String isReadOnly() default "";
	String isWritable() default "";
}
