package com.FlightPub.Services;

import com.FlightPub.model.WishListItem;
import com.FlightPub.repository.WishListItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements database interaction for wishlisting
 */
@Service("WishListServices")
public class WishListServices {

    private final WishListItemRepo wishlistRepo;

    /**
     * Sets the classes' repository object
     * @param wishlistRepository
     */
    @Autowired
    public WishListServices(WishListItemRepo wishlistRepository) {
        this.wishlistRepo = wishlistRepository;
    }

    /**
     * Gets a list of wish list items from specific users
     * @param userID
     * @return list of wish list items
     */
    public List<WishListItem> findAllByUserIDs(String userID) {
        return wishlistRepo.findAllByUserIDs(userID);
    }

    /**
     * Get a wish list item from id
     * @param id to find in the wish list repository
     * @return wishlistitem if id is found or null if not found
     */
    public WishListItem getById(String id) {
        return wishlistRepo.findById(id).orElse(null);
    }

    /**
     * Add a new wish list item or update a wish list item field in the database
     * @param toUpdate to save or update in the database
     * @return wish list item
     */
    public void saveOrUpdate(WishListItem toUpdate) {
        wishlistRepo.save(toUpdate);
    }

    /**
     * Deletes a wish list item from the database
     * @param id to find and delete the wish list item in the repository
     */
    public void delete(String id) {
    }

    /**
     * Returns list of the all wishlist items
     *
     *
     * @return list of wish list items
     */
    public List<WishListItem> listAll() {
        List<WishListItem> wishListItems = new ArrayList<>();
        wishlistRepo.findAll().forEach(wishListItems::add);
        return wishListItems;
    }

    /**
     * Returns list of the all wishlist items in descending order
     *
     *
     * @return sorted list of wish list items
     */
    public List<WishListItem> findAllByPopularitySortDesc() {
        return wishlistRepo.findAllByPopularitySortDesc();
    }
}

