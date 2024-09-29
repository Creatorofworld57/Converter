package ex.springsecurity_1805.Controllers;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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