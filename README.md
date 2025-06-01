## Recruitment Task – Simple App for Fetching Data and Writing It to Files

### Prerequisites
- **JDK 21** is required.

### How to Run the App

1. **Install dependencies and build the app:**

      **Linux / macOS:**
   ```bash
   ./mvnw clean install
   ```

   **Windows:**
   ```bash
   ./mvnw.cmd clean install
   ```

2. **Run**:

      **Linux / macOS:**
   ```bash
   ./mvnw exec:java -Dexec.mainClass="com.example.Main"
   ```

   **Windows:**
   ```bash
   ./mvnw.cmd exec:java -Dexec.mainClass="com.example.Main"
   ```

3. **Configure parameters if needed**

    **You can**
    1. Fetch comments with posts
    2. Specify output directory
    3. Provide default URL if needed

    **Example**

  ```bash
  ./mvnw exec:java -Dexec.mainClass="com.example.Main" -DfetchComments=true -DoutputDir="output_dir_name" -DapiUrl="https://jsonplaceholder.typicode.com/"
  ```

    **Default confg**

  ```bash
  ./mvnw exec:java -Dexec.mainClass="com.example.Main" -DfetchComments=false -DoutputDir="output" -DapiUrl="https://jsonplaceholder.typicode.com/"
  ```

