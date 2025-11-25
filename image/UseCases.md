UseCase 1

|Name:|sign in|
|---|---|
|Actor:| User|
|Description:| User inputs information to sign into application|
|Precondition:| N/A |
|Steps:| 1. User opens the application <br> 2. System requests sign in information (Username, schoolEmail (provided by school), Password) <br> 3. User provides the requested information <br> 4. System directs user to the Login page.|
|Result:| Users information is now in the database and login is possible.|

UseCase 2 

|Name:|log in|
|---|---|
|Actor:|User|
|Description:| User inputs required information to access the application|
|Precondition:| User has successfully signed in|
|Steps:| 1. User opens the application <br> 2. System requests users login information (schoolEmail, password) <br> 3. user provides requested information <br> 4. system grants user access to the application.|
|Result:| User successfully granted access to the application and is logged in|

UseCase 3

|Name:| User posts an item for sale|
|---|---|
|Actor:|User|
|Description:|User uploads item details to list it for sale on the platform |
|Precondition:|User is logged into the application|
|Steps:| 1.  User navigates to the "Post an Item" section <br> 2. System prompts user to fill in item details (e.g., title, description, price, category, etc.) <br> 3. User provides the required information and uploads images <br> 4. User submits the item listing. <br> 5. System verifies the provided information <br> 6. System confirms successful item listing|
|Result:| The item is successfully posted and visible to other users on the platform|
| Exception: | 4a User submits form with missing required fields or invalid price → System highlights missing fields and prevents submission <br> 5a System validation fails (e.g., price too high, inappropriate content) → System rejects listing and shows validation error <br> 6a Network error or server issue during submission → System displays error message and offers to retry submission |


UseCase 4

|Name:|User sends a message to a seller|
|---|---|
|Actor:|User|
|Description:|User sends a message to a seller to inquire about an item listed on the platform |
|Precondition:|User is logged into the application and viewing an item listing|
|Steps:| 1.  User opens the item listing page <br> 2.  User clicks the "Contact Seller" button <br> 3. System displays a message input field <br> 4. User types their message and clicks "Send". <br> 5. System delivers the message to the seller and confirms successful delivery|
|Result:| The seller receives the message, and the user is notified that their message was sent successfully|


UseCase 5
| **Name:** | User views an item listing |
|---|---|
| **Actor:** | User |
| **Description:** | User views the details of an item that is listed for sale on the platform |
| **Precondition:** | User is logged into the application and browsing items on the marketplace |
| **Steps:** | 1. User opens the application <br> 2. User navigates to the marketplace or search section <br> 3. User searches for or browses items <br> 4. User selects an item to view more details <br> 5. System displays the item details (e.g., title, description, price, images) |
| **Result:** | User successfully views the full details of the selected item |

Usecase 6

| **Name:** | User filters items by category |
|---|---|
| **Actor:** | User |
| **Description:** | User filters the marketplace items based on a specific category to narrow down search results |
| **Precondition:** | User is logged into the application and browsing items on the marketplace |
| **Steps:** | 1. User opens the marketplace section <br> 2. User clicks on the "Filter" or "Category" button <br> 3. System displays available categories <br> 4. User selects a category to filter items <br> 5. System updates the displayed items based on the selected category |
| **Result:** | User views items filtered by the selected category |


UseCase 7

| **Name:** | User marks an item as sold |
|---|---|
| **Actor:** | Seller |
| **Description:** | Seller updates their item listing to indicate that the item has been sold |
| **Precondition:** | Seller is logged into the application and has an active item listing |
| **Steps:** | 1. Seller navigates to their listed items <br> 2. Seller selects the item they want to mark as sold <br> 3. Seller clicks the "Mark as Sold" button <br> 4. System prompts the seller to confirm the action <br> 5. Seller confirms the action <br> 6. System updates the item status to "Sold" and removes it from active listings |
| **Result:** | The item is marked as sold and no longer visible in search results |

UseCase 8
| **Name:** | Seller updates item details |
|---|---|
| **Actor:** | Seller |
| **Description:** | Seller updates the details of an item that is already listed for sale |
| **Precondition:** | Seller is logged into the application and has an active item listing |
| **Steps:** | 1. Seller navigates to their listed items <br> 2. Seller selects the item they want to update <br> 3. Seller clicks the "Edit" or "Update" button <br> 4. System displays the current details of the item (e.g., title, description, price, images) <br> 5. Seller updates the desired fields (e.g., changing the price, adding more images, updating the description) <br> 6. Seller clicks "Save" or "Update" <br> 7. System saves the updated item details |
| **Result:** | The item details are successfully updated, and the new information is reflected in the listing |

UseCase 9
| **Name:** | Admin removes an inappropriate item listing |
|---|---|
| **Actor:** | Admin |
| **Description:** | Admin removes an item listing that violates the platform's policies or contains inappropriate content |
| **Precondition:** | Admin is logged into the application and the item is flagged for review |
| **Steps:** | 1. Admin logs into the application <br> 2. Admin navigates to the "Flagged Items" section <br> 3. Admin reviews the flagged item listing <br> 4. Admin clicks the "Remove" button to remove the listing <br> 5. System removes the item listing and notifies the seller of the removal |
| **Result:** | The item listing is removed from the platform, and the seller is notified |

UseCase 10:

|Name:| Review|
|---|---|
|Actor:| User|
|Description:| User provides review for a listing|
|Precondition:| User has an account and is logged in|
|Steps:| 1. User clicks on a listing.<br> 2. System directs user to the chosen listing.<br> 3. User inputs review in the text box under the listing and clicks on done.<br> 4. System displays the review.|
|Result:| User has successfully placed a review.|

UseCase 11:

|Name:| Edit profile|
|---|---|
|Actor:| User|
|Description:| User changes their account information|
|Precondition:| User has an account and is logged in|
|Steps:| 1. User clicks on profile.<br> 2. System provides user with choices (My listings, Setting).<br> 3. User clicks on Settings.<br> 4. System redirects user to the settings page.<br> 5. User clicks on edit profile.<br> 6.System prompts user to provide information about them.<br> 7. User provides the information and clicks on save.<br> 8. System saves new user information.|
|Result:| User has successfully changed their account information.|
| **Exception:** | 6a User leaves required fields empty and initiates "clicks on save" in step 7 → System highlights missing fields and prompts user to complete them <br>7a User enters invalid data (e.g., unsupported characters, invalid email format) → System displays validation error and requests correction <br>8a System fails to save changes due to a server error → System displays "Update Failed" message and allows user to retry |


UseCase 12:

|Name:| Search product|
|---|---|
| **Actor:** | User |
| **Description:** | User searches for specific items in the marketplace by entering keywords |
| **Pre-Condition:** | User is logged into the application |
| **Scenario:** | 1. User navigates to the search bar on the marketplace screen <br> 2. User enters a keyword (e.g., “bike”, “books”) <br> 3. System processes the search query <br> 4. System retrieves and displays matching items |
| **Result:** | User views a list of items matching the search query |
| **Exception:** | 2a User enters a blank → System prompts user to enter search term <br> 3a Network error during search → System displays "Connection Error" message and user will have to repeat step 2 <br> 4a No matching items found → System displays "No results found" message and user will have to repeat step 2 |




