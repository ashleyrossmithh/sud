package sud.domain.payment;


import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sud.core.bc.filter.api.FilterExpression;
import sud.core.dto.support.PageRequestByFilter;
import sud.core.dto.support.PageResponse;
import sud.domain.lawsuit.LawsuitRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentDTOServiceImpl implements PaymentDTOService {

    private final PaymentRepository paymentRepository;
    private final LawsuitRepository lawsuitRepository;


    @Override
    public void deleteById(Long id) {
        paymentRepository.deleteById(id);
    }

    @Override
    public PaymentDTO save(PaymentDTO dto) {
        if (dto == null) {
            return null;
        }
        final Payment item;
        if (dto.isIdOldSet()) {
            Payment attorneyTmp = paymentRepository.findById(dto.getIdOld()).orElse(null);
            if (attorneyTmp != null) {
                item = attorneyTmp;
            } else {
                item = new Payment();
                item.setId(dto.getIdOld());
            }
            PaymentDTO oldValue = toDTO(item);

        } else {
            if (dto.getId() != null && paymentRepository.findById(dto.getId()).orElse(null) != null)
                throw new ValidationException("Запись с данным ключом  " + dto.getId() + " уже существует");
            else
                item = new Payment();
        }
        updateFields(item, dto);
        return toDTO(paymentRepository.save(item));
    }

    public void updateFields(Payment item, PaymentDTO dto) {
        item.setId(dto.getId());
        item.setLawsuit(lawsuitRepository.getReferenceById(dto.getLawsuitId()));
        item.setNote(dto.getNote());
        item.setPayDate(dto.getPayDate());
        item.setPayPurpose(dto.getPayPurpose());
        item.setPaySumIn(dto.getPaySumIn());
        item.setPaySumOut(dto.getPaySumOut());
    }

    @Override
    public PaymentDTO toDTO(Payment attorney) {
        return toDTO(attorney, 1);
    }

    public PaymentDTO toDTO(Payment attorney, int depth) {
        return toDTO(attorney, depth, new PaymentDTO());
    }

    public PaymentDTO toDTO(Payment item, int depth, PaymentDTO dto) {
        if (item == null) {
            return null;
        }
        dto.setId(item.getId());
        dto.setLawsuitId(item.getLawsuit().getId());
        dto.setNote(item.getNote());
        dto.setPayDate(item.getPayDate());
        dto.setPayPurpose(item.getPayPurpose());
        dto.setPaySumIn(item.getPaySumIn());
        dto.setPaySumOut(item.getPaySumOut());
        return dto;
    }

    @Override
    public Payment toEntity(PaymentDTO dto) {
        return toEntity(dto, 1);
    }

    public Payment toEntity(PaymentDTO dto, int depth) {
        return toEntity(dto, depth, new Payment());
    }

    public Payment toEntity(PaymentDTO dto, int depth, Payment fromEntity) {
        if (dto == null) {
            return null;
        }
        updateFields(fromEntity, dto);
        return fromEntity;
    }

    @Override
    public List<PaymentDTO> getAllByLawsuitId(Long lawsuitId) {
        return paymentRepository.getAllByLawsuitId(lawsuitId).stream().map(this::toDTO).toList();
    }

    @Override
    public PageResponse<PaymentDTO> flexFilter(PageRequestByFilter<FilterExpression> var1) {
        System.out.println("-----------flexFilter");
        return null;
    }

    @Override
    public List<PaymentDTO> search(String var1, Object var2, int var3) {
        System.out.println("-----------search");
        return List.of();
    }

    @Transactional(readOnly = true)
    @Override
    public List<PaymentDTO> complete(String query, int maxResults, FilterExpression templateExpressions) {
        List<Payment> results = paymentRepository.findAll();
        return results.stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public PaymentDTO findOne(Long id) {
        PaymentDTO dto = toDTO(paymentRepository.findById(id).orElse(null));
        return dto;
    }
}
