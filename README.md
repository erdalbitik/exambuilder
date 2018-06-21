# EXAM BUILDER LIBRARY #

* Diğer uygulamalar tarafından kullanılacak olan, soruları PDF dokumanına basan library

### Amaç ###

* Bu kütüphane verilen soruları pdf dokumanına basar. 
* version 0.0.1

### Maven kurulumu ve kütüphanenin yüklenmesi ###

* Private maven repositorymiz (archiva gibi) olmadığından bu kütuphaneyi aşağıdaki yontemle kullanmak gerekiyor.
* Öncelikle sisteminize maven kurulumu yapın. Bunun için maven indirip bin klasorunu path değişkeni olarak gostermeniz gerekiyor.
* JAVA_HOME set edilmesi lazım. bunun için jdk klasorunuzu gosterin
* git kurulumu yapın. bunun için https://www.atlassian.com/git/tutorials/install-git adresinden indirip kurun.
* git clone https://github.com/erdalbitik/exambuilder.git komutunu çalıştırın. Parola ve kullanıcı adınızı girin.
* cd exambuilder diyerek proje klasorune girin.
* mvn clean install diyerek kutuphaneyi local maven'iniza eklemiş olursunuz.
* Bu adımdan itibaren projenize dependency olarak ekleyebilirsiniz.

```xml
<dependency>
     <groupId>com.ebitik</groupId>
     <artifactId>exambuilder</artifactId>
     <version>0.0.1</version>
</dependency>
```

### Yetenekler ###

* A4 Portrait baskı
* Çift kolonlu
* Tek kolonlu
* Çoktan seçmeli
* Grup soruları
* Açık uçlu (Essay- Klasik) soru
* Herhangi bir dilde baskı
* Buyuk Watermark (stamp)
* Kucuk watermark
* PDF seç-kopyala engelleme
* Sayfa numarası gösterme
* Sınav bitti etiketi
* Gömülü header (ad-soyad-no alanları)
* Verilen başlığı tek satırda gösterme (Sınav adı gibi)
* Soruları verilen sıraya gore basma

### Eksiklikler && Yapılması Gereken İşler ###

* HTML formatında header ve footer ekleme ozelliğinin olmayışı. Bunu ilerleyen aşamada yapmaya çalışacağım. 
* Gömülü kullanılan Türkçe label'lar. Örneğin Ad - soyad, Sınav Bitti, Sonraki sayfaya geçiniz gibi. Bunların verilen locale ile kullanılması lazım. Kullanıcı kaydedilirken onun locale bilgisi de alınmalı.
* Çoktan seçmeli bir sorunun tüm şıkları belli bir kısalıktaysa bunun şıklarını altalta gostermek yerine yan yana da gosterebilmeliyiz. Bu sayede yerden kazanç sağlamış oluruz.
* Sorular arası boşluğun kullanıcı tarafından verilebiliyor olması lazım. Şu anda sabit bir boşluk var. Bunu yapmamız işlem gerfektiren sorular için kolaylık sağlayacak.

### Kurulum ###

* Bu kütüphanenin kullanılması için öncelikle bazı uygulamaların kurulu olması gerekiyor. 
* Bunlar phantomjs ve/veya chrome puppeteer dir. Aşağıda bunları nasıl kuracağınızdan bahsedilmiştir.
* PhantomJS: http://phantomjs.org/download.html adresinden 2.1.1 phantomjs 'i indirerek kurun. Bunun komut satırında olduğundan emin olun (PATH'e eklenmelidir.)
* Puppeteer: Öncelikle nodejs güncel versiyonlardan biri kurulmalidir.
* Sonrasında yarn kurmanız gerekiyor. https://yarnpkg.com/lang/en/docs/install/ adresinden indirip kurabilirsiniz. 
* Ardından yarn add puppeteer komutunu çalıştırın. Puppeteer komutunun komut satırına geldiğinden emin olun.

### Kullanım ###

* MultipleChoiceTest dosyasındaki testleri inceliyiniz.