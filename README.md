[![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)](https://spring.io/)
[![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)](https://maven.apache.org/)
[![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)](https://jwt.io)
<a href="https://www.linkedin.com/in/fryderyk-jachimczak/"><img align="right" src="https://img.shields.io/badge/linkedin-%230077B5.svg?style=for-the-badge&logo=linkedin&logoColor=white"></a>


<div align="center">
<h1 align="center">bike-shop-backend</h1>

  <p align="center">
   Backend for online bike shop build with <a href="https://spring.io/guides/gs/spring-boot/">Spring Boot</a>
    <br />
    <a href="https://github.com/qloqloq/eshop-backend"><strong>Explore the docs »</strong></a>
    <br />
    <br />

  </p>
</div>

<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">built with</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Quick start</a>
      <ul>
        <li><a href="#prerequisites">command line</a></li>
        <li><a href="#installation">maven plugin</a></li>
      </ul>
    </li>
    <li><a href="#improvements">Planned improvements</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>

## About The Project

This is my first more extensive project build from scratch by myself. Main goal of this repo was to ground my knowledge about
Spring Boot, SQL and security. For now project is done with basic functionality and won't be developed for some time.
You will find GUI and demo upon 
this <a href="https://github.com/qloqloq/eshop-frontend-angular"> link </a>.

### Built With

* [Spring Boot](https://spring.io/)
* [Maven](https://maven.apache.org/)
* [Junit 5](https://junit.org/junit5/)
* [Hibernate](https://hibernate.org/)
* [PostgreSQL](https://www.postgresql.org/)
* [Jwt](https://jwt.io)
* [Apache Commons Lang3](https://commons.apache.org/)

## Quick start

### command line

This [Spring Boot](https://spring.io/guides/gs/spring-boot/) application is built
using [Maven](https://spring.io/guides/gs/maven/). You can build a jar file and run it from the command line (it should
work just as well with Java 8, 11).

  ```sh
git clone https://github.com/qloqloq/bike-shop-spring.git
cd bike-shop-spring
./mvnw package
java -jar target/*.jar
  ```

### maven plugin

Or you can run it from Maven directly using the Spring Boot Maven plugin

  ```sh
./mvnw spring-boot:run
  ```

Then go to http://localhost:8080/

## Planned improvements

- [ ] implement [Facade pattern](https://www.sihui.io/design-pattern-facade/) for placing orders
- [ ] count user daily login
- [ ] total spent money by user
- [ ] add product to favorite

## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any
contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also
simply open an issue with the tag "enhancement". Don't forget to give the project a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## Acknowledgments

List resources I find helpful and would like to give credit to.

* [Cool README.md template](https://github.com/othneildrew/Best-README-Template)
