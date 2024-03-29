
CREATE TABLE ID
(
    USER_ID        INT         NOT NULL,

    PRIMARY KEY (USER_ID)
);

CREATE TABLE USERS
(
	EMAIL_ADDRESS  VARCHAR(50) NOT NULL,
	POSTAL_ADDRESS VARCHAR(50) NOT NULL,
	PASSWRD        VARCHAR(50) NOT NULL,
	NAME           VARCHAR(50) NOT NULL,
	LAST_NAME      VARCHAR(50) NOT NULL,
	USER_ID        INT         NOT NULL,

	PRIMARY KEY (EMAIL_ADDRESS),
	FOREIGN KEY (USER_ID)      REFERENCES ID
);



CREATE TABLE ID_PRODUCT
(
    PRODUCT_ID        INT         NOT NULL,
    CATEGORY_NOM      VARCHAR(50) NOT NULL,

    PRIMARY KEY (PRODUCT_ID)
    FOREIGN KEY (CATEGORY_NOM) REFERENCES SOUS_CATEGORY
);

CREATE TABLE CATEGORY
(
    CATEGORY_NAME  VARCHAR(50) NOT NULL,

    PRIMARY KEY (CATEGORY_NAME)
);

CREATE TABLE SOUS_CATEGORY
(
    CATEGORY_NOM  VARCHAR(50) NOT NULL,
    CATEGORY_NAME VARCHAR(50) NOT NULL,

    PRIMARY KEY (CATEGORY_NOM),
    FOREIGN KEY (CATEGORY_NAME) REFERENCES CATEGORY
);


CREATE TABLE PRODUCT
(
	PRODUCT_ID     INT           NOT NULL,
	CURRENT_PRICE  FLOAT(20)     NOT NULL,
	DESCRIPTION    VARCHAR(2000) NOT NULL,
	CATEGORY_NOM  VARCHAR(50)   NOT NULL,
	INTITULE       VARCHAR(1000) NOT NULL,
	URL_PHOTO      VARCHAR(100)  NOT NULL,

    PRIMARY KEY (PRODUCT_ID),
    FOREIGN KEY (CATEGORY_NOM) REFERENCES SOUS_CATEGORY,
    FOREIGN KEY (PRODUCT_ID)      REFERENCES ID_PRODUCT,
	  CONSTRAINT   CURRENT_PRICE  CHECK      (CURRENT_PRICE > 0)
);




CREATE TABLE OFFERS
(
	DATE_TIME      TIMESTAMP        NOT NULL,
	PRODUCT_ID     INT         NOT NULL,
	OFFERED_PRICE  FLOAT       NOT NULL,
    USER_ID        INT         NOT NULL,

	PRIMARY KEY (DATE_TIME),
	FOREIGN KEY (PRODUCT_ID)   REFERENCES ID_PRODUCT,
    FOREIGN KEY (USER_ID)      REFERENCES ID,

    CONSTRAINTS  OFFERED_PRICE CHECK      (OFFERED_PRICE > 0)
);





CREATE TABLE CARACTERISTICS
(
      CARAC          VARCHAR(50) NOT NULL,
      VALUE          VARCHAR(50)         NOT NULL,
      PRODUCT_ID     INT         NOT NULL,

      PRIMARY KEY (CARAC, PRODUCT_ID) ,
      FOREIGN KEY (PRODUCT_ID) REFERENCES ID_PRODUCT
);
