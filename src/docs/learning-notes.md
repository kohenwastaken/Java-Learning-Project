# Learning Notes

Bu dosyada banka projesini geliştirirken öğrendiğim Java ve proje geliştirme notlarını tutuyorum.

---

## Scanner Kullanımı

`Scanner` ile kullanıcıdan veri alırken `nextInt()` sadece sayıyı okur, Enter karakterini okumaz.

Bu yüzden `nextInt()` sonrası `nextLine()` kullanılmazsa, bir sonraki `nextLine()` boş string okuyabilir.

Örnek mantık:

```java
int choice = sc.nextInt();
sc.nextLine(); // kalan Enter karakterini temizler
```
## String İşlemleri

Ad ve soyadı ayrımak için bazı `string` işlemleri öğrendim.

`trim()` Stringin başındaki ve sonundaki boşlukları temizler.

```java
name = name.trim();
```

`lastIndexOf(" ")` String içindeki son boşluğun indexini bulur. 
Ad-soyad ayrımında kullanışlıdır çünkü soyad genelde son kelime olarak alınabilir.

```java
int index = name.lastIndexOf(" ");
```

`substring()` Stringin belli bir bölümünü kesip almak için kullanılır.

```java
String firstName = name.substring(0, index);
String lastName = name.substring(index + 1);
```
---
## Nesne Oluşturma
Bir class, nesneler için şablondur.
```java
Customer customer1 = new Customer(firstName, lastName, password, accID);
```
burada `customer1`, `Customer` classından oluşan bir nesnedir.

---

## ArrayList içinde nesne saklama

Birden fazla müşteri tutmak için `ArrayList<Customer>` kullanılabilir.

```java
ArrayList<Customer> customerList = new ArrayList<Customer>();
customerList.add(customer);
```
listeye eklenen şey değişkenin ismi değil, değişken(ler)in kendisi eklenir.

---

## Döngü ile ArrayList içinde arama

Bir müşteri ID'ye göre listede aranabilir.
```java
Customer requestedCustomer = null;

for (Customer customer : customerList) {
        if (customer.accID == accID) {
requestedCustomer = customer;
        break;
                }
}
```
Eğer müşteri bulunursa `requestedCustomer` o nesneyi döndürür.
Bulunamazsa `null` kalır.

---

## null Kontrolü

Bir nesne bulunamamış olabilir. o yüzden nesnenin fieldlarına erişmeden önce `null` konrtrolü yapılmalıdır.

```java
if (requestedCustomer == null) {
    IO.println("Belirtilen ID yoktur.");
    return null;
}
```
Bu kontrol yapılmazsa program `nullPointerException` verebilir.

---

## String Karşılaştırma

Javada String içeriklerini karşılaştırmak için `==` değil `.equals()` kullanılmalıdır.

```java
enteredPassword == requestedCustomer.pswrd //yanlış
enteredPassword.equals(requestedCustomer.pswrd) // doğru
```

`==` iki nesnenin bellekte aynı nesne olup olmadığına bakar.
`.equals()` ise String içeriklerini karşılaştırır.

---

## Methodlara Parametre Gönderme

Bir methodun dışarıdan bilgi alması için parametreler kullanılır.

```java
static Customer login(Scanner sc, List<Customer> customerList)
```

Burada method içine:

* kullanııcıdan veri almak için `Scanner`
* kayıtlı müşteriler arasında seçim yapmak için `customerList`

 gönderilir.
 
---

## Methoddan değer döndürme

Bir method işlem yaptıktan sonra sonuç döndürebilir.

```java
return requestedCustomer; //giriş başarılıysa müşteri nesnesi döner.
return null; //giriş başarısızsa null döner.
```
bu sayede şu mantık kurulabilir

```java
Customer loggedInCustomer = login(sc, customerList);

if (loggedInCustomer == null) {
    break;
}
```

---

## break ve return farkı

`break` içinde bulunduğu döngüden veya switchden çıkar.
`return` methoddan çıkar.

# Şu Ana Kadar Öğrenilen Ana Konular
* Scanner kullanımı

* nextInt() ve nextLine() ilişkisi

* String parçalama

* trim(), lastIndexOf(), substring()

* Nesne oluşturma

* Constructor kullanımı

* ArrayList içinde nesne saklama

* For-each döngüsü

* String karşılaştırmada .equals()

* null kontrolü

* Method parametreleri

* Method return mantığı

* Login işleminde nesne döndürme fikri