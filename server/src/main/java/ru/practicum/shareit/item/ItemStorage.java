package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ItemStorage extends JpaRepository<Item, Long> {

    Collection<Item> getAllItemsByOwnerId(Long userId);

    @Query (
            """
            select i from Item i
            where (
            upper(i.name) like upper(concat('%', :text, '%'))
            or upper(i.description) like upper(concat('%', :text, '%'))
            )
            and i.available = true
            """

    )
    Collection<Item> searchItemsByNameOrDescription(String text);

    boolean existsByIdAndOwnerId(Long id, Long userId);

    Collection<Item> searchItemsByItemRequest_Id(Long id);

    Collection<Item> searchItemsByItemRequestIdIn(Collection<Long> ids);

}
