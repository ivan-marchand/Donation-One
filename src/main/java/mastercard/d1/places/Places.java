package mastercard.d1.places;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import mastercard.d1.business.Charity;
import mastercard.d1.db.SqliteDBConnect;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import com.google.gson.Gson;

public class Places {
	
	protected static final Logger LOGGER = Logger.getLogger(Places.class.getName());
	
	
	static class POI {
		String CleansedMerchantName;
		String MerchantStreetAddress;
		String MerchantStateProvidenceCode;
		String MerchantPostalCode;
	}
	
	static class EncapsObj {
		Vector<POI> MerchantPOI;
	}
	
	static class EncapsList {
		int Count;
		EncapsObj MerchantPOIArray;
	}
	
	static class ResponseMerchantPOI {
		EncapsList MerchantPOIList;
	}
	
	public static class Address {
		String address;
		Charity charity;
	}

	public static Vector<Address> callPlaces(String iPostalCode){
				
		Vector<Address> result = new Vector<Address>();
		
		HttpClient httpclient = new DefaultHttpClient();
        try {
        	
       
            HttpGet httpget = new HttpGet("http://dmartin.org:8026/merchantpoi/v1/merchantpoisvc.svc/merchantpoi?PostalCode="+iPostalCode+"&MCCCode=8398&Format=json");

            System.out.println("executing request " + httpget.getURI());

            // Create a response handler
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(httpget, responseHandler);
			
            System.out.println("----------------------------------------");
            System.out.println(responseBody);
            System.out.println("----------------------------------------");
            
            Gson aGson = new Gson();
            ResponseMerchantPOI aData = aGson.fromJson(responseBody, ResponseMerchantPOI.class);
            
            HashMap<Integer,Address> map_charities = new HashMap<Integer,Address>();
            for (POI poi:aData.MerchantPOIList.MerchantPOIArray.MerchantPOI){
            	LOGGER.debug(poi.CleansedMerchantName);
            	
            	Vector<Charity> chars = Charity.searchCharity(poi.CleansedMerchantName);
            	for (Charity charity:chars){
            		Address addr = new Address();
                	addr.address = poi.MerchantStreetAddress + ", " + poi.MerchantStateProvidenceCode+ " "+poi.MerchantPostalCode;
                	addr.charity = charity;
            		map_charities.put(charity.id, addr);
            	}
            	
            }
            
            for (Address addr:map_charities.values()){
            	result.add(addr);
            }

        } catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }
        
        return result;
		
	}
	
	
}
