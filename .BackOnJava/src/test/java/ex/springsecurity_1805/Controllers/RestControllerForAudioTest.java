package ex.springsecurity_1805.Controllers;

<<<<<<< HEAD
=======
>>>>>>> 28f0fe1eeab61e9b089570a81e3064ad1acdb625
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

<<<<<<< HEAD
=======

>>>>>>> 28f0fe1eeab61e9b089570a81e3064ad1acdb625
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class RestControllerForAudioTest {
    @Mock
    private AudioRepository audioRepository;

    @InjectMocks
    RestControllerForAudio restControllerForAudio;

    @Test
    public void getAudioName(){
        var names = new Audio();
        names.setName("Soda Luv Дождь");
        doReturn(names).when(audioRepository).findAll();

        var responseEntity= this.restControllerForAudio.audioName(152L);

        assertNotNull(responseEntity);


    }
}