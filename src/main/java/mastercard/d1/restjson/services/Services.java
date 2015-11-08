package mastercard.d1.restjson.services;

import java.util.Vector;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import mastercard.d1.business.Category;
import mastercard.d1.business.Charity;
import mastercard.d1.business.Payment;
import mastercard.d1.business.Result;
import mastercard.d1.business.User;
import mastercard.d1.places.Places;
import mastercard.d1.places.Places.Address;

import org.apache.log4j.Logger;

@Path("/v1")
public class Services {

	protected static final Logger LOGGER = Logger.getLogger(Services.class.getName());
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/simple")
	public String[] simple( @Context SecurityContext sc ) {
		LOGGER.info("Returning result ...");
		return new String[]{"This is a test"};
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getCategoryList")
	public Category[] getCategoryList( @Context SecurityContext sc) {
		return Category.returnCategoryList();
	}
		
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getCharityList")
	public Vector<Charity> getCharityList( @Context SecurityContext sc, @QueryParam("category") String iCategory, @QueryParam("zipcode") String iZipCode) {
		LOGGER.info("Request charity from category:"+iCategory+" "+iZipCode);
		return Charity.retrieveCharityByCategoryAndZipcode(iCategory, iZipCode);
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/processPayment")
	public Result processPayment( @Context SecurityContext sc, Payment iPayment) {
		LOGGER.info("Processing Payment ...");
		return Payment.processPayment(iPayment);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getCharityById")
	public Charity getCharityById( @Context SecurityContext sc, @QueryParam("id") Integer iId) {
		LOGGER.info("Request charity by id:"+iId);
		return Charity.retrieveCharityById(iId);
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/searchCharity")
	public Vector<Charity> getCharityById( @Context SecurityContext sc, @QueryParam("text") String iText) {
		LOGGER.info("Search charity by text:"+iText);
		return Charity.searchCharity(iText);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/retrieveUser")
	public User[] retrieveUser( @Context SecurityContext sc, @QueryParam("email") String iEmail) {
		LOGGER.info("Search user by email:#"+iEmail+"#");
		return new User[]{User.retrieveUser(iEmail)};
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/placesRetrieve")
	public Vector<Address> placesRetrieve( @Context SecurityContext sc, @QueryParam("zipcode") String iZipCode) {
		LOGGER.info("Place retrieve by zip code:#"+iZipCode+"#");
		return Places.callPlaces(iZipCode);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/oneClickPayment")
	public Result oneClickPayment( @Context SecurityContext sc, @QueryParam("email") String iEmail, @QueryParam("amount") String iAmount) {
		LOGGER.info("One Click Payment for user "+iEmail+" ...");
		return Payment.oneClickPayment(iEmail, iAmount);
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/simplePost")
	public Object simplePost( @Context SecurityContext sc,
									   String[] iList) { 
		return null;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("/{state}/otherMIME/")
	public byte[] simpleOtherMIMEType( @Context SecurityContext sc, @PathParam("state") String iState) throws Exception {
		try {
			return "Stream".getBytes();
		} catch (Exception e) {
			LOGGER.info("Simple Error");
			throw new NotFoundException(e.getMessage());		
		}
		
	}
	
	
	
	
}
