    package com.example.EducationSell.Helper.mapper;

    import com.example.EducationSell.DTO.BankAccountDTO;
    import com.example.EducationSell.Model.BankAccount;
    import org.mapstruct.Mapper;
    import org.mapstruct.Mapping;
    import org.mapstruct.factory.Mappers;

    @Mapper(componentModel = "spring")
    public interface BankAccountMapper {
        BankAccountMapper INSTANCE = Mappers.getMapper(BankAccountMapper.class);

        @Mapping(target = "instructor", ignore = true) // Handle User separately
        @Mapping(target = "bankAccountId", ignore = true) // ID is auto-generated
        @Mapping(target = "createdAt", ignore = true) // Set by @PrePersist
        BankAccount toEntity(BankAccountDTO bankAccountDTO);
    }
