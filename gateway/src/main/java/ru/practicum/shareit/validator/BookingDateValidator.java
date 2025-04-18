package ru.practicum.shareit.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.shareit.booking.dto.NewBookingRequest;

public class BookingDateValidator implements ConstraintValidator<ValidBookingDates, NewBookingRequest> {

    @Override
    public boolean isValid(NewBookingRequest request, ConstraintValidatorContext context) {
        if (request.getStart() == null || request.getEnd() == null) {
            return true;
        }

        return request.getEnd().isAfter(request.getStart());
    }
}
