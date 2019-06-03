## Tutorial
1. Create an folder (e.g. car) and execute the command ``jhipster`` in this folder.

![Initial Screen](https://github.com/luukaass/E-Portfolio-JHipster/blob/master/pictures/installJHipster.PNG)

	* "Monolithic application"
	* ENTER
	* "de.dhbw.cars"
	* ENTER
	* ENTER
	* ENTER
	* H2 with in-memory persistence
	* ENTER
	* Maven / Gradle
	* Enter
	* Enter
	* No
	* Cucumber

![Finished setup](https://github.com/luukaass/E-Portfolio-JHipster/blob/master/pictures/installJHipsterComplete.PNG)

2. Create the entities ``Owner`` and ``Car`` with the [JDL Studio](https://start.jhipster.tech/jdl-studio/). Please find details how to create the JHipster Domain Language [here](http://jhipster.github.io/jdl/).
3. Entity ``Owner`` should have following attributes:
	* Firstname (reqired)
	* Lastname (required)
	* Birthyear
4. Entitiy ``car`` should have following attributes:
	* Model
	* Company
	* Dateofproduction
5. Add the following relation to these two entities:
	* One ``car`` has exactly one ``owner``
	* One ``Owner`` can have multiple cars

6. Import the generated JDL file into the project by using the ``jhipster import-jdl filename.jh`` command.

![Completed Import](https://github.com/luukaass/E-Portfolio-JHipster/blob/master/pictures/ImportSuccess.PNG)

7. Start the application using the ``mvnw`` command and add entities.

![Application running](https://github.com/luukaass/E-Portfolio-JHipster/blob/master/pictures/Running.PNG)

8. Import the app into your IDE and run tests