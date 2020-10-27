import kong.unirest.Unirest;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    private static final ArrayList<String> strCurrency = new ArrayList<String>();

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        if (args.length == 2) {

            String xml = Unirest.get("http://www.cbr.ru/scripts/XML_daily.asp")
                    .queryString("date_req", args[0])
                    .asString()
                    .getBody()
                    .replace("windows-1251", "UTF-8");

            File file = new File("xml.xml");
            FileWriter fr = null;
            try {
                fr = new FileWriter(file);
                fr.write(xml);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(file);

            NodeList currencyElements = document.getElementsByTagName("Valute");

            for (int i = 0; i < currencyElements.getLength(); i++) {
                Node currency = currencyElements.item(i);
                NodeList a = currency.getChildNodes();
                for (int j = 0; j < a.getLength(); j++) {
                    Node cur = a.item(j);
                    if (cur.getTextContent().equals(args[1])) {
                        Node b = cur.getParentNode();
                        NodeList c = b.getChildNodes();
                        for (int k = 0; k < c.getLength(); k++) {
                            Node curr = c.item(k);
                            strCurrency.add(curr.getTextContent());
                        }
                        break;
                    }
                }
            }
            try {
                Currency theCurrency = new Currency(strCurrency.get(3), strCurrency.get(2), strCurrency.get(4));
                System.out.println(theCurrency.getNominal() + " " + theCurrency.getCurrency() + " = " + theCurrency.getValue() + " Российских рубля");
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Ошибка: неправильная дата или идентификатор валюты. Пожалуйста, введите два аргумента:" +
                        "\n● Дату в формате dd/MM/yyyy например 21/02/2019\n" +
                        "● Идентификатор валют, например USD или JPY");
            }
        } else {
            System.out.println("Ошибка: неправильное количество аргументов. Пожалуйста, введите два аргумента:" +
                    "\n● Дату в формате dd/MM/yyyy например 21/02/2019\n" +
                    "● Идентификатор валют, например USD или JPY");
        }

    }
}