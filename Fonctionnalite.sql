1) Connexion du client à l’application avec son email comme identifiant et vérification du mot de
passe.

-- Le client entre un email et un mot de passe, si ceux-ci appartiennent à un
--utilisateur dans la base de données, une connexion sera établie.


2) Parcours des catégories et sous-catégories

-- Après avoir établi une connexion, le client choisit de passer par les catégories.
-- Le client choisit une catégorie, puis toutes les sous-catégories de cette
-- catégorie particulière seront affichées. S'il choisit ensuite une sous-catégorie,
--  tous les produits de cette sous-catégorie seront affichés. Chaque produit est
--  présenté sur une ligne avec son nom, sa description et le nombre d'offres
--  faites sur ce produit. L'ordre des offres est décroissant.



SELECT CATEGORY_NOM FROM SOUS_CATEGORY WHERE CATEGORY_NAME = 'Books';


SELECT INTITULE,CURRENT_PRICE,DESCRIPTION, URL_PHOTO, PRODUCT.PRODUCT_ID,
COUNT(OFFERS.PRODUCT_ID) AS Nb_offres FROM PRODUCT LEFT JOIN OFFERS ON PRODUCT.PRODUCT_ID = OFFERS.PRODUCT_ID
WHERE CATEGORY_NOM='novels' GROUP BY PRODUCT.PRODUCT_ID, INTITULE, CURRENT_PRICE,DESCRIPTION, URL_PHOTO ORDER BY Nb_offres DESC, UPPER(INTITULE);


3) Parcours des catégories recommandées

-- Une autre option que le client peut choisir est de parcourir les catégories par
-- recommandation en fonction de son activité. S'il choisit cette option, une liste
-- de catégories s'affichera. Ces catégories sont une combinaison entre les catégories
-- pour lesquelles il a fait le plus d'offres et les catégories pour lesquelles il a
-- fait le plus d'offres en moyenne en termes de produits.
-- S'il choisit une catégorie parmi ces catégories, ses sous-catégories seront
-- affichées et la même chose se répétera comme avant.

SELECT CATEGORY_NAME, COUNT(O.PRODUCT_ID) AS CNT
FROM SOUS_CATEGORY S
JOIN ID_PRODUCT P ON S.CATEGORY_NOM=P.CATEGORY_NOM
JOIN OFFERS O ON O.PRODUCT_ID = P.PRODUCT_ID
WHERE O.USER_ID NOT IN(

  SELECT USER_ID
  FROM OFFERS
  WHERE DATE_TIME IN
   (
   SELECT MAX(DATE_TIME)
   FROM OFFERS
   WHERE OFFERS.PRODUCT_ID = P.PRODUCT_ID
   GROUP BY PRODUCT_ID
   HAVING  COUNT(PRODUCT_ID)=5
   )
)
and O.USER_ID = '10'
GROUP BY CATEGORY_NAME
ORDER BY CNT DESC
--------------------------------------------------------------------------------
SELECT CATEGORY_NAME
FROM(
  SELECT CATEGORY_NAME, COUNT(O.PRODUCT_ID)/COUNT(P.PRODUCT_ID) AS CNT
  FROM SOUS_CATEGORY S
  JOIN ID_PRODUCT P ON S.CATEGORY_NOM=P.CATEGORY_NOM
  LEFT JOIN OFFERS O ON O.PRODUCT_ID = P.PRODUCT_ID
  GROUP BY CATEGORY_NAME
  ORDER BY CNT DESC
    )
WHERE CATEGORY_NAME NOT IN (
  SELECT CATEGORY_NAME FROM(
   SELECT CATEGORY_NAME, COUNT(O.PRODUCT_ID) AS CNT
   FROM SOUS_CATEGORY S
   JOIN ID_PRODUCT P ON S.CATEGORY_NOM=P.CATEGORY_NOM
   JOIN OFFERS O ON O.PRODUCT_ID = P.PRODUCT_ID
   WHERE O.USER_ID NOT IN(
      SELECT USER_ID
      FROM OFFERS
      WHERE DATE_TIME IN
       (
       SELECT MAX(DATE_TIME)
       FROM OFFERS
       WHERE OFFERS.PRODUCT_ID = P.PRODUCT_ID
       GROUP BY PRODUCT_ID
       HAVING  COUNT(PRODUCT_ID)=5
     )
  )
 and O.USER_ID = 10
 GROUP BY CATEGORY_NAME
 ORDER BY CNT DESC
 ));


4) Enchères

-- Dans les étapes ci-dessus, lorsque la liste des produits est affichée, le client
-- peut faire une offre sur un produit. Si le client clique sur un produit, une fenêtre
-- dans laquelle il peut faire son offre apparaîtra, la nouvelle offre sera acceptée
-- si le prix est supérieur au prix actuel. Si l'offre est acceptée, elle sera
-- enregistrée et un message apparaîtra dans le terminal (offre enregistrée). Et si
-- l'offre est la cinquième sur le produit, le produit sera retiré de la liste des
-- produits et le client remportera le produit.

INSERT INTO OFFERS VALUES(sysdate, '3', '123.5', '1');
UPDATE PRODUCT SET CURRENT_PRICE = '123.5' WHERE PRODUCT_ID = '3';


5) Droit à l’oubli

-- L'utilisateur peut supprimer son compte. Il lui suffit, après une connexion, de
-- cliquer sur le bouton (Supprimer le compte). Le compte sera alors supprimé,
-- mais l'identifiant de l'utilisateur sera stocké dans une table séparée et y
-- restera pour toujours.
-- Le client ne peut plus se connecter avec son email et son mot de passe, mais les
-- offres qu'il a déjà faites resteront dans la table des offres et seront prises
-- en compte.

DELETE FROM USERS WHERE EMAIL_ADDRESS = 'email@gmail.com';
