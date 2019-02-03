# CodeGenerationDemo
CodeGenerationDemo

## code snippet 

> added annotations: 

 ```kotlin
 
 @BindListener
interface Listener {

    @BindAction(actionName = "GO_ONE")
    fun goOne()

    @BindAction(actionName = "GO_TWO")
    fun goTwo()
}

 
 ```
 >calling generated methods
 
  ```kotlin
  
    bindFields_Listener_generateMethod(this, "GO_ONE")
    bindFields_Listener_generateMethod(this, "GO_TWO")
   
 ```
 
 > generated code: 
 
   ```kotlin
   
   fun bindFields_Listener_generateMethod(listener: Listener, action: String) {
    when{
    action == "GO_ONE" -> {
             listener.goOne()
            }

    action == "GO_TWO" -> {
             listener.goTwo()
            }

    }
}

   
    ```
