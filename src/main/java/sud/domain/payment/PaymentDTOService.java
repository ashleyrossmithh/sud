package sud.domain.payment;

import sud.core.dto.support.IUpdatableBaseDTOService;
import sud.dto.AttorneyToPersonsDTO;

import java.util.List;

public interface PaymentDTOService extends IUpdatableBaseDTOService<PaymentDTO, Payment, PaymentRepository, Long> {


 //   CourtDTO createNew();

    PaymentDTO findOne(Long id);

    void updateFields(Payment item, PaymentDTO dto);

    PaymentDTO save(PaymentDTO dto);

    /**
     * Converts the passed Court to a DTO.
     */
    PaymentDTO toDTO(Payment item);

    /**
     * Converts the passed Court to a DTO.
     */
    default PaymentDTO toDTO(Payment item, int depth) {
        return toDTO(item, depth, new PaymentDTO());
    }


    PaymentDTO toDTO(Payment item, int depth, PaymentDTO fromDTO);

    /**
     * Converts the passed dto to a Court.
     * Convenient for query by example.
     */
    default Payment toEntity(PaymentDTO dto) {
        return toEntity(dto, 1);
    }

    /**
     * Converts the passed dto to a Court.
     * Convenient for query by example.
     */
    default Payment toEntity(PaymentDTO dto, int depth) {
        return toEntity(dto, depth, new Payment());
    }

    /**
     * Converts the passed dto to a Court.
     * Convenient for query by example.
     */
    Payment toEntity(PaymentDTO dto, int depth, Payment fromEntity);

    List<PaymentDTO> getAllByLawsuitId(Long lawsuitId);
}
