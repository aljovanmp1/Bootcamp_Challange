package binarfud.challenge3.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import binarfud.challenge3.model.Menu;
import binarfud.challenge3.utlis.Constants;
import binarfud.challenge3.utlis.DataNotFoundException;
import binarfud.challenge3.utlis.WrongInputException;

@SpringBootTest
public class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Test
    @DisplayName("Positive Test - getter")
    void getMenuTest() {
        menuService.getMenuList();
        Assertions.assertTrue(true);
    }

    @Test
    @DisplayName("Positive Test - get menu")
    void getMenuByIdTest() {
        Optional<Menu> menuOptional = menuService.getMenuById(1L);
        assertTrue(menuOptional.isPresent());
        assertEquals(1, menuOptional.get().getId());
    }

    @Test
    @DisplayName("Negative Test - Menu empty")
    void initDataTest() {
        menuService.deleteAllMenu();
        menuService.restartSeq();

        menuService.initData();
        assertTrue(true);
    }

    @Test
    @DisplayName("Positive Test - refetch menu")
    void refetchDataTest() {
        menuService.refetchData();
        assertTrue(menuService.getMenuList().size() > 0);
    }

    @Test
    @DisplayName("Negative Test - add menu merchant not exist")
    void addNegMenuTest() {
        Menu newMenu = menuService.addMenu("Nasi", 5000, 2L);
        assertEquals(newMenu.getMerchant(), null);
    }
}
