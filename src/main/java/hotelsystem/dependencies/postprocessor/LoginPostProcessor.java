package hotelsystem.dependencies.postprocessor;

public class LoginPostProcessor implements BeanPostProcessor{
    @Override
    public void process(Object bean) {
        System.out.println(String.format( "Login: bean has been created: %s", bean.getClass()));
    }
}