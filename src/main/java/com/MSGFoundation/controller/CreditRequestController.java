package com.MSGFoundation.controller;

import com.MSGFoundation.dto.CreditInfoDTO;
import com.MSGFoundation.model.Couple;
import com.MSGFoundation.model.CreditRequest;
import com.MSGFoundation.model.Person;
import com.MSGFoundation.service.CreditRequestService;
import com.MSGFoundation.service.MarriedCoupleService;
import com.MSGFoundation.util.RequestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("credit_request")
public class CreditRequestController {
    private final CreditRequestService creditRequestService;
    private final CoupleController coupleController;
    private final PersonController personController;
    private final MarriedCoupleService marriedCoupleService;

    @Autowired
    public CreditRequestController(CreditRequestService creditRequestService, CoupleController coupleController, PersonController personController, MarriedCoupleService marriedCoupleService){        this.creditRequestService = creditRequestService;
        this.coupleController = coupleController;
        this.personController = personController;
        this.marriedCoupleService = marriedCoupleService;
    }

    @GetMapping("/")
    public List<CreditRequest> getAllCreditRequest(){
        return creditRequestService.getAllCreditRequest();
    }

    @GetMapping("/{coupleId}")
    public ResponseEntity<List<CreditRequest>> findCreditRequestByCouple(@PathVariable Long coupleId){
        try{
            Couple couple = coupleController.getCoupleById(coupleId);
            List<CreditRequest> creditRequest = creditRequestService.findCreditByCouple(couple);
            return ResponseEntity.ok(creditRequest);
        }catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/create")
    public RedirectView createCreditRequest(@ModelAttribute CreditInfoDTO creditInfoDTO,
                                            @RequestParam("pdfSupport") MultipartFile pdfSupport,
                                            @RequestParam("workSupport") MultipartFile workSupport,
                                            RedirectAttributes redirectAttributes) {
        List<Person> people = creditInfoDTO.getPeople();
        Person partner1 = people.get(0);
        Person partner2 = people.get(1);

        personController.createPerson(creditInfoDTO);

        CreditRequest creditRequest = new CreditRequest();
        creditRequest.setMarriageYears(creditInfoDTO.getMarriageYears());
        creditRequest.setBothEmployees(creditInfoDTO.getBothEmployees());
        creditRequest.setHousePrices(creditInfoDTO.getHousePrices());
        creditRequest.setQuotaValue(creditInfoDTO.getQuotaValue());
        creditRequest.setCoupleSavings(creditInfoDTO.getCoupleSavings());
        creditRequest.setStatus(RequestStatus.DRAFT.toString());
        LocalDateTime currentDate = LocalDateTime.now();
        creditRequest.setRequestDate(currentDate);
        Long coupleId = coupleController.getCouplebyIds(partner1.getId(), partner2.getId()).getBody();
        Couple couple = coupleController.getCoupleById(coupleId);
        creditRequest.setApplicantCouple(couple);
        creditRequest.setCountReviewCR(0L);


        String pdfSupportName = String.format("%s-%s-pdf-support.pdf", partner1.getId(), partner2.getId());
        String workSupportName = String.format("%s-%s-work-support.pdf", partner1.getId(), partner2.getId());

        creditRequest.setPdfSupport(pdfSupportName);
        creditRequest.setWorkSupport(workSupportName);

        creditInfoDTO.setApplicantCoupleId(coupleId);
        creditInfoDTO.setRequestDate(currentDate);
        creditInfoDTO.setCountReviewCR(0L);

        creditInfoDTO.setPdfSupportName(pdfSupportName);
        creditInfoDTO.setWorkSupportName(workSupportName);
        creditRequest.setPayment(false);


        redirectAttributes.addAttribute("coupleId",coupleId);

        creditRequestService.createCreditRequest(creditRequest);
        List<CreditRequest> updateCredit = creditRequestService.findCreditByCouple(couple);
        creditInfoDTO.setCodRequest(updateCredit.get(0).getCodRequest());
        String processId = marriedCoupleService.startProcessInstance(creditInfoDTO);

        for (CreditRequest request : updateCredit) {
            if ("DRAFT".equals(request.getStatus())) {
                request.setProcessId(processId);
                creditRequestService.updateCreditRequest(request.getCodRequest(), request);
            }
        }

        String destinationFolder = Paths.get(System.getProperty("user.dir"), "credit-supports").toString();

        try {
            File folder = new File(destinationFolder);
            if (!folder.exists()) {
                if (folder.mkdirs()) {
                    System.out.println("Carpeta creada correctamente");
                } else {
                    System.err.println("Error al crear la carpeta");
                }
            }

            String pdfFilePath = Paths.get(destinationFolder, pdfSupportName).toString();
            pdfSupport.transferTo(new File(pdfFilePath));

            String workSupportFilePath = Paths.get(destinationFolder, workSupportName).toString();
            workSupport.transferTo(new File(workSupportFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new RedirectView("/view-credit");
    }

    @PostMapping("/update")
    public RedirectView updateCreditRequest(@ModelAttribute("creditInfoDTO") CreditInfoDTO creditInfoDTO){

        List<Person> people = creditInfoDTO.getPeople();
        personController.updatePerson(people.get(0).getId(),people.get(0));
        personController.updatePerson(people.get(1).getId(),people.get(1));

        Long coupleId = coupleController.getCouplebyIds(people.get(0).getId(), people.get(1).getId()).getBody();
        Couple couple = coupleController.getCoupleById(coupleId);
        List<CreditRequest> creditId = creditRequestService.findCreditByCouple(couple);

        CreditRequest creditRequest = new CreditRequest();
        creditRequest.setMarriageYears(creditInfoDTO.getMarriageYears());
        creditRequest.setBothEmployees(creditInfoDTO.getBothEmployees());
        creditRequest.setHousePrices(creditInfoDTO.getHousePrices());
        creditRequest.setQuotaValue(creditInfoDTO.getQuotaValue());
        creditRequest.setCoupleSavings(creditInfoDTO.getCoupleSavings());
        creditRequest.setApplicantCouple(couple);
        creditRequest.setStatus(RequestStatus.DRAFT.toString());
        LocalDateTime currentDate = LocalDateTime.now();
        creditRequest.setRequestDate(currentDate);
        creditRequest.setProcessId(creditId.get(0).getProcessId());

        creditRequestService.updateCreditRequest(creditId.get(0).getCodRequest(), creditRequest);
        String result = marriedCoupleService.updateProcessVariables(creditRequest.getProcessId(),creditRequest);

        return new RedirectView("/view-credit?coupleId="+result);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCreditRequest(@PathVariable Long id){
        creditRequestService.deleteCreditRequest(id);
    }

    @GetMapping("/findbyid/{processId}")
    public CreditRequest getCreditRequestByProcessId(@PathVariable String processId){
        return creditRequestService.getCreditRequestByProcessId(processId);
    }

    @GetMapping("/latest-credit-request")
    public CreditRequest getLatestCreditRequest() {
        return creditRequestService.findFirstByOrderByRequestDateDesc();
    }
}
