# Architecture Decisions

Bu dosyada banka projesinde alınan tasarım kararları ve gerekçeleri tutulur.

---

## 1. Customer class olarak tasarlandı

### Karar

`Customer` bir `class` olarak oluşturulacak.

### Gerekçe

Customer sistemde gerçek bir varlığı temsil eder. İçinde veri tutar:

- ad
- soyad
- şifre
- hesap ID

Bu yüzden `interface` değil, `class` olması daha uygundur.

## 2. Veriler başlangıçta ArrayList içinde tutulacak

### Karar

Müşteriler başlangıçta şu yapıda tutulacak:

```java
ArrayList<Customer> customerList = new ArrayList<Customer>();
```
### Gerekçe
Projenin ilk aşamasında PostgreSQL’e geçmeden önce nesne ve liste mantığını öğrenmek hedefleniyor.
`ArrayList`, program çalışırken bellekte veri saklamak için uygundur.

### Not
Program kapanınca `ArrayList` içindeki veriler silinir.
İleride PostgreSQL’e geçildiğinde veriler kalıcı hale getirilecektir.

## 3. Kullanıcı adı yerine accID kullanılacak

### Karar
Giriş işleminde kullanıcı adı veya ad-soyad yerine `accID` kullanılacak.

### Gerekçe
Projede ayrıyeten kullanıcı adı istenilmemiş. ad-soyad ise başkası ile aynı olabilir.

## 4. Login methodu boolean değil Customer döndürecek

### Karar
Login methodu başarılı girişte `Customer`, başarısız girişte `null` döndürecek.

### Gerekçe
Sadece `boolean pass` döndürmek girişin başarılı olup olmadığını gösterir ama kimin giriş yaptığını göstermez.
Customer döndürmek daha kullanışlıdır çünkü giriş yapan kullanıcıya ait bilgiler direkt elde edilir.

## 5. Giriş sonrası hesap işlemleri ayrı methoda taşınacak

### Karar
Giriş başarılı olduktan sonra hesap işlemleri `case 2` içinde uzun uzun yazılmayacak. Bunun yerine ayrı bir method kullanılacak.

### Gerekçe
`main` methodunun çok büyümesini engeller, daha temiz bir görev akışı sağlar.

## 6. main methodu sadece akışı yönetecek

### Karar

`main` içinde tüm detaylar yazılmayacak. Detay işlemler methodlara bölünecek.

### Gerekçe

Kod okunabilirliği artar SOLID prensiplerine uyulur.

