package org.jz.orahle;



import java.util.List;

public interface ConnectionService {

	/**
	 * Retrieve all cars in the catalog.
	 * @return all cars
	 */
	public List<DbConnection> findAll();
	
	/**
	 * search cars according to keyword in model and make.
	 * @param keyword for search
	 * @return list of car that match the keyword
	 */
	public List<DbConnection> search(String keyword);
}
