package mastercard.d1.business;

import com.simplify.payments.*;
import com.simplify.payments.exception.ApiCommunicationException;
import com.simplify.payments.exception.AuthenticationException;
import com.simplify.payments.exception.InvalidRequestException;
import com.simplify.payments.exception.NotAllowedException;
import com.simplify.payments.exception.SystemException;

public class Payment {

	String id;
	String amount;
	String cardNumber;
	String cardExpiryMonth;
	String cardExpiryYear;
	String cardCvc;
	
	public static Result processPayment(Payment iPayment){

		Result aResult = new Result();
		aResult.result = "KO";
		
		Charity charity = Charity.retrieveCharityById(Integer.parseInt(iPayment.id));
		
		PaymentsApi.PUBLIC_KEY = charity.public_key;
		PaymentsApi.PRIVATE_KEY = charity.private_key;
		//To Test
		//PaymentsApi.PUBLIC_KEY = "sbpb_NGRhN2NlNDUtZDkzYy00MWYzLTg1YWItYjcxZDRlYWE0NmQx";
		//PaymentsApi.PRIVATE_KEY = "siQ2yDvZnPXPcGB9TA27rdmCIj5QYxVsHZKRhsWVHW55YFFQL0ODSXAOkNtXTToq";
		
		com.simplify.payments.domain.Payment payment;
		try {
			
			Double amountValue = new Double(Double.parseDouble(iPayment.amount)*100);
			Integer amountInCents = amountValue.intValue();
			
			payment = com.simplify.payments.domain.Payment.create(new PaymentsMap()
											.set("currency", "USD")
											.set("card.cvc", iPayment.cardCvc)
											.set("card.expMonth", Integer.parseInt(iPayment.cardExpiryMonth))
											.set("card.expYear", Integer.parseInt(iPayment.cardExpiryYear))
											.set("card.number", iPayment.cardNumber)
											.set("amount", amountInCents) // In cents e.g. 1234 for $12.34
											.set("description", "Payment to "+charity.name));
		
			if ("APPROVED".equals(payment.get("paymentStatus"))) {
				aResult.result = "OK";
			}
			
		} catch (ApiCommunicationException e) {
			e.printStackTrace();
		} catch (AuthenticationException e) {
			e.printStackTrace();
		} catch (InvalidRequestException e) {
			e.printStackTrace();
		} catch (NotAllowedException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return aResult;

	}
    
}
