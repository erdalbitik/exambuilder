# EXAM BUILDER LIBRARY #

* Diğer uygulamalar tarafından kullanılacak olan, soruları PDF dokumanına basan library

### Amaç ###

* Bu kütüphane verilen soruları pdf dokumanına basar. 
* version 0.0.1

### Maven ###

* Öncekilikle sisteminize maven kurulumu yapın. Bunun için maven klasorunu indirip path içinde gostermeniz gerekiyor.
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
* watermark
* PDF seç-kopyala engelleme
* sayfa numarası gösterme
* sınav bitti etiketi
* Gömülü header (ad-soyad-no alanları)
* Verilen başlığı tek satırda gösterme (Sınav adı gibi)
* Soruları verilen sıraya gore basma

### Kurulum ###

* BU kütüphanenin kullanılması için öncelikle bazı uygulamaların hazır olması gerekiyor. 
* Bunlar phantomjs ve chrome puppeteer dir. Şağıda bunları nasıl kuracağınızdan bahsedilmiştir.
* http://phantomjs.org/download.html adresinden 2.1.1 phantomjs 'i indirrerek kurun. Bunun komut satırında olduğundan emin olun (PATH'e eklenmelidir.)
* Öncelikle nodejs kululmalidir. v7.6.0 LTE ve yukarısından birini kurun. 
* sonrasında yarn kurmanız gerekiyor. https://yarnpkg.com/lang/en/docs/install/ adresinden indirip kurabilirsiniz. 
* ardından yarn add puppeteer komutunu çalıştırın. puppeteer komutunun komut satırına geldiğinden emin olun.

### Kullanım ###

* MultipleChoiceTest dosyasındaki testleri inceliyiniz.