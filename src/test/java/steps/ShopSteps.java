package steps;

import io.cucumber.java.en.*;
import org.testng.Assert;
import pages.*;
import utils.Memory;
import utils.TestUtils;

public class ShopSteps {

    private final HomePage home = new HomePage();
    private final SignupModal signup = new SignupModal();
    private final LoginModal login = new LoginModal();
    private final CategoryPage category = new CategoryPage();
    private final ProductPage product = new ProductPage();
    private final CartPage cart = new CartPage();
    private final OrderModal order = new OrderModal();

    /* ======= Header/Modal actions ======= */

    @When("I open the {string} modal")
    public void i_open_the_modal(String which) {
        // Eat any stale alerts so header is clickable
        TestUtils.acceptAlertIfPresent(1);

        if ("Sign up".equalsIgnoreCase(which)) {
            home.openSignupModal();
            signup.waitOpen();
        } else if ("Log in".equalsIgnoreCase(which) || "Login".equalsIgnoreCase(which)) {
            home.openLoginModal();
            login.waitOpen();
        } else {
            Assert.fail("Unknown modal: " + which);
        }
    }

    @And("I sign up with a fresh random username and password")
    public void i_sign_up_with_random_credentials() {
        String[] creds = signup.signUpRandom();
        Memory.saveCreds(creds[0], creds[1]);
    }

    @Then("I should see an alert containing {string}")
    public void i_should_see_an_alert_containing(String expected) {
        String txt = TestUtils.getLastAlertOrWait(6);
        Assert.assertNotNull(txt, "Expected an alert to appear.");
        Assert.assertTrue(txt.contains(expected),
                "Alert text mismatch. Expected to contain: " + expected + " but was: " + txt);
    }

    @And("I log in with the last signed-up credentials")
    public void i_log_in_with_the_last_signed_up_credentials() {
        // if the sign-up alert is *still* present, accept it
        TestUtils.acceptAlertIfPresent(1);

        login.login(Memory.getUser(), Memory.getPass());
    }

    @Then("I should be logged in as the last signed-up user")
    public void i_should_be_logged_in_as_the_last_signed_up_user() {
        Assert.assertTrue(home.isLoggedInAs(Memory.getUser()),
                "Welcome badge did not show expected user.");
    }

    /* ======= Category & Products ======= */

    @When("I navigate to {string} category")
    public void i_navigate_to_category(String categoryName) {
        home.openCategory(categoryName);
        category.waitGridReady();
    }

    @And("I add product {string} to the cart")
    public void i_add_product_to_the_cart(String productName) {
        category.openProductByName(productName);
        String alertText = product.addToCartAndGetAlert();
        // Be lenient to slow alerts
        if (alertText == null) {
            alertText = TestUtils.getLastAlertOrWait(5);
        }
        Assert.assertTrue(alertText != null && alertText.toLowerCase().contains("added"),
                "Add-to-cart alert missing.");
    }

    @And("I return to the product grid")
    public void i_return_to_the_product_grid() {
        // navigate back to home, the HomePage.openCategory will handle going to the desired grid later
        try {
            // Back once to the category grid; if still on PDP, back again to be safe
            utils.DriverFactory.getDriver().navigate().back();
            Thread.sleep(300);
        } catch (Exception ignored) {}
    }

    /* ======= Cart ======= */

    @Then("I open the cart")
    public void i_open_the_cart() {
        home.openCart();
        cart.waitForCart();
    }

    @And("I should see items {string} present in the cart")
    public void i_should_see_items_present_in_the_cart(String csv) {
        cart.waitForCart();
        String[] expected = csv.split("\\s*,\\s*");
        var titles = cart.getItemTitles();
        for (String e : expected) {
            Assert.assertTrue(titles.contains(e), "Missing item in cart: " + e + " — titles: " + titles);
        }
    }

    @And("the cart total should equal the sum of the item prices")
    public void the_cart_total_should_equal_the_sum_of_the_item_prices() {
        cart.waitForCart();
        int sum = cart.sumPrices();
        int total = cart.getDisplayedTotal();
        Memory.setCartTotal(total);
        Assert.assertEquals(total, sum, "Displayed cart total != computed sum.");
    }

    /* ======= Checkout ======= */

    @When("I place the order")
    public void i_place_the_order() {
        order.clickPlaceOrder();
        order.waitOpen();
    }

    @Then("the modal total should equal the cart total")
    public void the_modal_total_should_equal_the_cart_total() {
        int modalTotal = order.getModalTotal();
        Integer cartTotal = Memory.getCartTotal();
        Assert.assertNotNull(cartTotal, "Cart total was not captured earlier.");
        Assert.assertEquals(modalTotal, cartTotal.intValue(),
                "Order modal total != cart total.");
    }

    @When("I fill checkout details {string}, {string}, {string}, {string}, {string}, {string}")
    public void i_fill_checkout_details(String name, String country, String city,
                                        String card, String month, String year) {
        order.fill(name, country, city, card, month, year);
    }

    @And("I purchase")
    public void i_purchase() {
        order.purchase();
    }

    @Then("I should see a success message")
    public void i_should_see_a_success_message() {
        Assert.assertTrue(order.successVisible(), "Success dialog not shown or text mismatch.");
    }
}
