# SSLValidationQuery
SOEN 321: Information Systems Security

* Alexa rank file top-1m.csv from 8 Oct 2015

##Description

* Study the prevalence of HTTPS along with various security parameters in the Top 1 million most popular websites

##Features

Multi-threaded application
* executing multiple queries concurrently.
* better performance!!!

query a website and get these informations : 
  * is https supported?
  * With which SSL/TLS version did you connect to the websited?
  * which type of public key is presented in the server's certificated (RSA,DSA or EC)?
  * what is the key size
  * which algorithm is used in the server's certificate for certificate signature (MD5, SHA1, SHA56...)?
  * Does the server support Strict Transport Policy
  * if it does, does it use a max-age of a month or more?
