The Inventory Management System is designed to monitor products in a user's inventory and keep them informed about incoming orders. It offers a comprehensive solution for managing products, suppliers, and orders. This system allows users to track product availability, manage suppliers, and handle order processing efficiently.

This tool is tailored for entities that oversee multiple products from various suppliers. Users can create customer orders, which automatically deduct quantities from the product inventory.

This project focuses on developing a backend for the inventory management system using RESTful APIs, with Spring Boot and Hibernate for database interactions. There will be no user interface; instead, the system will be tested and validated using Postman to ensure the APIs function correctly and reliably


1. User Management
    User Registration
    - Users can register by providing a unique email and password.
    - The system will validate the email and password before creating a new user account.
    - The system will generate a unique user ID for each new user.
   
    User Login
    - Users can log in using their email and password.
    - The system will validate the user's credentials before granting access.
    - The system will generate a unique session token for the user to maintain their login status.

2. Role Based Access Control
    Admin Role
    - Admin users have full access to all system functionalities.
    - Admin users can create, read, update, and delete products, suppliers, and orders.
    - Admin users can view all users' information and update their roles.

    User Role
    - User have limited access to system functionalities.
    - User can create, read, and delete their own orders.
    - User can read products information.

3. Supplier Management
    - Admin users can create, read, update, and delete suppliers.
    - Suppliers have a unique supplier ID, name, and contact information.
    - Suppliers can have multiple products associated with them.
   
4. Product Management
    - Admin users can create, read, update, and delete products.
    - Products have a unique product ID, name, description, price, and quantity.
    - Multiple products can be associated with a supplier.

5. Order Management
    - Users can create, read, and delete their own orders.
    - Orders have a unique order ID, user ID, product ID, quantity, and order status.
    - Orders can be associated with a specific product and user.
    - Orders can be in one of the following statuses: PENDING, PROCESSING, SHIPPED, DELIVERED.
    - Orders will automatically deduct the product quantity from the inventory when created.
    - Orders can be updated to change the order status.

6. CSV Import/Export
    - Admin users can import and export products and suppliers data using CSV files.
    - The system will validate the CSV file format before importing data.
    - The system will export data to a CSV file in a readable format. 
