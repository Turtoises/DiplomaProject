package by.tms.tmsmyproject.mapers;

import by.tms.tmsmyproject.dto.item.ItemResponseDeleteDto;
import by.tms.tmsmyproject.entities.Item;
import by.tms.tmsmyproject.dto.item.ItemResponseGetDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, BookMapper.class})
public interface ItemMapper {

    ItemResponseGetDto toGetDto(Item item);

    ItemResponseDeleteDto toDeleteDto(Item item);
}
