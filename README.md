# EXAM BUILDER LIBRARY #

* Diğer uygulamalar tarafından kullanılacak olan, soruları PDF dokumanına basan library

### Amaç ###

* Bu kütüphane verilen soruları pdf dokumanına basar. 
* version 0.0.1

### Maven ###

* Private maven repositorymiz (archiva gibi) olmadığından bu kıutuphaneyi aşağıdaki yontemle kullanmak zorundayız.
* Öncelikle sisteminize maven kurulumu yapın. Bunun için maven indirip bin klasorunu path içinde gostermeniz gerekiyor.
* JAVA_HOME set edilmesi lazım. bunun için jdk klasorunuzu gosterin
* git kurulumu yapın. bunun için https://www.atlassian.com/git/tutorials/install-git adresinden indirip kurun
* git clone https://erdalbitik@bitbucket.org/sinavapp/exambuilder.git komutunu çalıştırın. Parola ve kullanıcı adınızı girin.
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
* buyuk Watermark (stamp)
* Kucuk watermark
* PDF seç-kopyala engelleme
* sayfa numarası gösterme
* sınav bitti etiketi
* Gömülü header (ad-soyad-no alanları)
* Verilen başlığı tek satırda gösterme (Sınav adı gibi)
* Soruları verilen sıraya gore basma

### Eksiklikler ###

* 20 soruluk bir pdf oluşturma işlemi eğer soruların boyutu onceden belirlenmediyse 20-25 sn surebiliyor. (Kendi laptopumda)
* HTML formatında header ve footer ekleme ozelliğinin olmayışı. Bunu ilerleyen aşamada yapmaya çalışacağım. 
* Gömülü kullanılan Türkçe label'lar. Örneğin Ad - soyad, Sınav Bitti, Sonraki sayfaya geçiniz gibi. Bunların verilen locale ile kullanılması lazım. Kullanıcı kaydedilirken onun locale bilgisi de alınmalı.
* Maven repository problemi. Bunun için private repository kullanmak lazım yada bir maven repository bulmak lazım.

### Kurulum ###

* Bu kütüphanenin kullanılması için öncelikle bazı uygulamaların hazır olması gerekiyor. 
* Bunlar phantomjs ve chrome puppeteer dir. Şağıda bunları nasıl kuracağınızdan bahsedilmiştir.
* http://phantomjs.org/download.html adresinden 2.1.1 phantomjs 'i indirrerek kurun. Bunun komut satırında olduğundan emin olun (PATH'e eklenmelidir.)
* Öncelikle nodejs kululmalidir. v7.6.0 LTE ve yukarısından birini kurun. 
* sonrasında yarn kurmanız gerekiyor. https://yarnpkg.com/lang/en/docs/install/ adresinden indirip kurabilirsiniz. 
* ardından yarn add puppeteer komutunu çalıştırın. puppeteer komutunun komut satırına geldiğinden emin olun.

### Kullanım ###

* MultipleChoiceTest dosyasındaki testleri inceliyiniz.