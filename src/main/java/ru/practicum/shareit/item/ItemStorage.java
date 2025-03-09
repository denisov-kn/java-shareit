package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface ItemStorage extends JpaRepository<Item, Long> {

    Collection<Item> getAllItemsByOwnerId(Long userId);

    @Query (
            " select i from Item i " +
            "where (" +
                    " ( upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%'))" +
             " ) and i.available = true )"
    )
    Collection<Item> searchItemsByNameAndDescription(String text);

    boolean existsByIdAndOwnerId(Long id, Long userId);


}
