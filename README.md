# monticore-commons
The main goal of this project is to integrate [MontiCore](http://monticore.org/) into other libraries,
in order to introduce model-based code-generation.

This project will then serve as a core module for further projects.

## Currently supported

* The different time intervals in [chart](https://github.com/Zavarov/chart)

* Several entities of one of the inofficial [Discord API's](https://github.com/DV8FromTheWorld/JDA)
  * Mentioned users, roles and channels
  * The different online status' of users
  * Most of the permissions

* A lightweight interface for comments and submissions of one of the inofficial [Reddit API's](https://github.com/mattbdean/JRAW)

* Simple arithmetic operations.
  * Primitive operators **+**, **-**, **&#x2A;**, **/**, **%** *(modulo)* and **^** *(power)*
  * Primitive functions such as *sin(x)* and *log(x)*
  * Constants &#x2107; and &#x3C0; as e and pi, respectively
  * (semi-)random number generation via *rand(x,y)*
  
* An interface for custom [Discord](https://discordapp.com/) bots
  * Custom commands
  * Global and per-guild configuration files
  * Internal permission structure
  * Status messages
  * Command generator

## Built With

* [MontiCore](https://github.com/MontiCore/monticore) - For generating and parsing the commands as well as handling the configuration files.
* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Zavarov**

## Dependencies:

This project requires at least **Java 8**.  
Due to an internal dependency of [MontiCore](https://github.com/MontiCore/monticore), we can't use a newer version of [Guava](https://github.com/google/guava) than **23.0**.
 * **Apache Commons Text**
   * Version: **1.8**
   * [Github](https://github.com/apache/commons-text)
 * **bot**
   * Version: **3.3.5**
   * [Github](https://github.com/Zavarov/discord-bot)
 * **chart**
   * Version: **1.1.6**
   * [Github](https://github.com/Zavarov/chart)
 * **Evo Inflector**
   * Version: **1.2.2**
   * [Github](https://github.com/atteo/evo-inflector)
 * **JDA**
   * Version: **4.0.0_53**
   * [Github](https://github.com/DV8FromTheWorld/JDA)
 * **MontiCore**
   * Version: **5.3.0**
   * [Github](https://github.com/MontiCore/monticore)
 * **reddit**
   * Version: **2.2**
   * [Github](https://github.com/Zavarov/reddit)

## Plugins:
 * **Apache Maven Jar Plugin**
   * Version: **3.1.2**
   * [Github](https://github.com/apache/maven-jar-plugin)
 * **Apache Maven JavaDoc Plugin**
   * Version: **3.1.1**
   * [Github](https://github.com/apache/maven-javadoc-plugin)
 * **Apache Maven Source Plugin**
   * Version: **3.1.0**
   * [Github](https://github.com/apache/maven-source-plugin)
 * **MontiCore Maven Plugin**
   * Version: **5.3.0**
   * [Github](https://github.com/MontiCore/monticore/tree/master/monticore-maven/monticore-maven-plugin)
## Test Dependencies:
 * **AssertJ**
   * Version: **3.12.2**
   * [Github](https://github.com/joel-costigliola/assertj-core)
 * **JUnit**
   * Version: **4.12**
   * [Github](https://github.com/junit-team/junit4)

## License

This project is licensed under the GPLv3 License - see the [LICENSE](LICENSE) file for details

## Acknowledgments
Many thanks to the Chair of Software Engineering for developing MontiCore.
Even though I had to curse quite a lot due to some of its quirks (and me being an idiot), it saved me quite a lot of work. :D