CREATE trigger ins_shopify
AFTER INSERT
ON
Products
FOR EACH ROW
INSERT INTO Shopify( Title, Body_HTML, Variant_Price,Variant_Inventory_Qty,Variant_Barcode, Type, Tags, Handle, Variant_SKU)
VALUES (
(SELECT DescriptiveText
FROM PLUS
WHERE CodeText = NEW.CodeNumber),
(SELECT DescriptiveText
FROM PLUS
WHERE CodeText = NEW.CodeNumber),
(SELECT Prices11
FROM PLUS
WHERE CodeText = NEW.CodeNumber),
(SELECT StockCurrent
FROM PLUS
WHERE CodeText = NEW.CodeNumber),
NEW.CodeNumber,
NEW.GroupName,
NEW.Tags,
LOWER(REPLACE((SELECT DescriptiveText
FROM PLUS
WHERE CodeText = NEW.CodeNumber)," ","-")),
SUBSTRING((SELECT DescriptiveText FROM PLUS WHERE CodeText = NEW.CodeNumber) 
 FROM  (LOCATE("#",(SELECT DescriptiveText FROM PLUS WHERE CodeText = NEW.CodeNumber)) + 1) 
 FOR (LOCATE(" ",(SELECT DescriptiveText FROM PLUS WHERE CodeText = NEW.CodeNumber), LOCATE("#",(SELECT DescriptiveText FROM PLUS WHERE CodeText = NEW.CodeNumber)))) - (LOCATE("#",(SELECT DescriptiveText FROM PLUS WHERE CodeText = NEW.CodeNumber))))
)