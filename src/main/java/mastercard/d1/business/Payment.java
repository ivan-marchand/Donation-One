package mastercard.d1.business;

import java.text.SimpleDateFormat;
import java.util.Date;

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
	String email;
	
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
				
				History hist = new History();
				
				if (iPayment.email == null || iPayment.email == "")
					hist.id_user = -1;
				else {
					User aUser = User.retrieveUser(iPayment.email);
					hist.id_user = aUser.id;
				}
				    hist.amount = iPayment.amount;
				    hist.id_ch = charity.id;
					hist.dateTime =new SimpleDateFormat().format(new Date());
					
					History.createRecord(hist);
			
				
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
    
	public static Result oneClickPayment(String iEmail, String iAmount){

		Result aResult = new Result();
		aResult.result = "KO";
		
		User aUser = User.retrieveUser(iEmail);

		Payment aPayment = new Payment();
		aPayment.cardNumber = aUser.ccn;
		aPayment.cardExpiryMonth = aUser.expiry_month;
		aPayment.cardExpiryYear = aUser.expiry_year;
		aPayment.cardCvc = aUser.cvc;
		aPayment.email = iEmail;

		//Get only first charity
		int id_ch = aUser.links.entrySet().iterator().next().getValue().id_charity;
		aPayment.id = String.valueOf(id_ch);
		if (iAmount == null || iAmount == "")
			aPayment.amount = String.valueOf(aUser.links.entrySet().iterator().next().getValue().oneclick);
			
		else
			aPayment.amount = iAmount;
		
		aResult = processPayment(aPayment);
		
		
		return aResult;
		
	}
}
