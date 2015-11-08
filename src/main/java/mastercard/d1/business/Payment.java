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
	
	public static String processPayment(Payment iPayment){

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
			return "OK";
			}
			else {
				return "KO";
			}
			
		} catch (ApiCommunicationException e) {
			e.printStackTrace();
			return "KO";
		} catch (AuthenticationException e) {
			e.printStackTrace();
			return "KO";
		} catch (InvalidRequestException e) {
			e.printStackTrace();
			return "KO";
		} catch (NotAllowedException e) {
			e.printStackTrace();
			return "KO";
		} catch (SystemException e) {
			e.printStackTrace();
			return "KO";
		}
		    
	}
    
}
