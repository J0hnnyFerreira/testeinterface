package model.services;

import java.util.Calendar;
import java.util.Date;

import model.entities.Contract;
import model.entities.Installment;

public class ContractService {
	
	private OnlinePaymentService paymentService;
	
	public ContractService(OnlinePaymentService paymentService) {
		this.paymentService = paymentService;
	}

	public void processContract(Contract contract, int months) {
		double basicQuota = contract.getTotalValue() / months;
		for (int i = 1; i <= months; i++) {
			double updateQuota = basicQuota + paymentService.interest(basicQuota, months);
			
			double fullQuota = updateQuota + paymentService.paymentFee(updateQuota);
			
			Date dueDate = addMonths(contract.getDate(), i);
			
			contract.getIstallments().add(new Installment(dueDate,fullQuota));
		}
		
	}
	
	private Date addMonths(Date date, int N) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, N);
		return calendar.getTime();
	}
}
