package ru.practicum.shareit.validator;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = BookingDateValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBookingDates {
    String message() default "Время окончания бронирования должно быть после времени начала";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
