package mastercard.d1.business;

import com.simplify.payments.*;
import com.simplify.payments.exception.ApiCommunicationException;
import com.simplify.payments.exception.AuthenticationException;
import com.simplify.payments.exception.InvalidRequestException;
import com.simplify.payments.exception.NotAllowedException;
import com.simplify.payments.exception.SystemException;

public class Payment {

	Integer id;
	String amount;
	String cardNumber;
	String cardExpiryMonth;
	String cardExpiryYear;
	String cardCvc;
	
	public static String processPayment(){
        
        PaymentsApi.PUBLIC_KEY = "sbpb_NGRhN2NlNDUtZDkzYy00MWYzLTg1YWItYjcxZDRlYWE0NmQx";
        PaymentsApi.PRIVATE_KEY = "siQ2yDvZnPXPcGB9TA27rdmCIj5QYxVsHZKRhsWVHW55YFFQL0ODSXAOkNtXTToq";
        com.simplify.payments.domain.Payment payment;
		try {
			payment = com.simplify.payments.domain.Payment.create(new PaymentsMap()
			                                 .set("currency", "USD")
			                                 .set("card.cvc", "123")
			                                 .set("card.expMonth", 11)
			                                 .set("card.expYear", 19)
			                                 .set("card.number", "5555555555554444")
			                                 .set("amount", 60) // In cents e.g. $0.60
			                                 .set("description", "Payment to Charity"));
		
			if ("APPROVED".equals(payment.get("paymentStatus"))) {
	            return "Payment OK";
	        }
	        else {
	            return "Payment KO";
	        }
			
		} catch (ApiCommunicationException e) {
			e.printStackTrace();
			return "Payment KO";
		} catch (AuthenticationException e) {
			e.printStackTrace();
			return "Payment KO";
		} catch (InvalidRequestException e) {
			e.printStackTrace();
			return "Payment KO";
		} catch (NotAllowedException e) {
			e.printStackTrace();
			return "Payment KO";
		} catch (SystemException e) {
			e.printStackTrace();
			return "Payment KO";
		}
        
	}
    
}
