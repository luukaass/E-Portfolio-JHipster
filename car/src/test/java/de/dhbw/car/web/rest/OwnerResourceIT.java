package de.dhbw.car.web.rest;

import de.dhbw.car.CarApp;
import de.dhbw.car.domain.Owner;
import de.dhbw.car.repository.OwnerRepository;
import de.dhbw.car.service.OwnerService;
import de.dhbw.car.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static de.dhbw.car.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link OwnerResource} REST controller.
 */
@SpringBootTest(classes = CarApp.class)
public class OwnerResourceIT {

    private static final String DEFAULT_FIRSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRSTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_LASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_LASTNAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_BIRTHYEAR = 1;
    private static final Integer UPDATED_BIRTHYEAR = 2;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restOwnerMockMvc;

    private Owner owner;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OwnerResource ownerResource = new OwnerResource(ownerService);
        this.restOwnerMockMvc = MockMvcBuilders.standaloneSetup(ownerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Owner createEntity(EntityManager em) {
        Owner owner = new Owner()
            .firstname(DEFAULT_FIRSTNAME)
            .lastname(DEFAULT_LASTNAME)
            .birthyear(DEFAULT_BIRTHYEAR);
        return owner;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Owner createUpdatedEntity(EntityManager em) {
        Owner owner = new Owner()
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .birthyear(UPDATED_BIRTHYEAR);
        return owner;
    }

    @BeforeEach
    public void initTest() {
        owner = createEntity(em);
    }

    @Test
    @Transactional
    public void createOwner() throws Exception {
        int databaseSizeBeforeCreate = ownerRepository.findAll().size();

        // Create the Owner
        restOwnerMockMvc.perform(post("/api/owners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(owner)))
            .andExpect(status().isCreated());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeCreate + 1);
        Owner testOwner = ownerList.get(ownerList.size() - 1);
        assertThat(testOwner.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testOwner.getLastname()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testOwner.getBirthyear()).isEqualTo(DEFAULT_BIRTHYEAR);
    }

    @Test
    @Transactional
    public void createOwnerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ownerRepository.findAll().size();

        // Create the Owner with an existing ID
        owner.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOwnerMockMvc.perform(post("/api/owners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(owner)))
            .andExpect(status().isBadRequest());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllOwners() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList
        restOwnerMockMvc.perform(get("/api/owners?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(owner.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME.toString())))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME.toString())))
            .andExpect(jsonPath("$.[*].birthyear").value(hasItem(DEFAULT_BIRTHYEAR)));
    }
    
    @Test
    @Transactional
    public void getOwner() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get the owner
        restOwnerMockMvc.perform(get("/api/owners/{id}", owner.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(owner.getId().intValue()))
            .andExpect(jsonPath("$.firstname").value(DEFAULT_FIRSTNAME.toString()))
            .andExpect(jsonPath("$.lastname").value(DEFAULT_LASTNAME.toString()))
            .andExpect(jsonPath("$.birthyear").value(DEFAULT_BIRTHYEAR));
    }

    @Test
    @Transactional
    public void getNonExistingOwner() throws Exception {
        // Get the owner
        restOwnerMockMvc.perform(get("/api/owners/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOwner() throws Exception {
        // Initialize the database
        ownerService.save(owner);

        int databaseSizeBeforeUpdate = ownerRepository.findAll().size();

        // Update the owner
        Owner updatedOwner = ownerRepository.findById(owner.getId()).get();
        // Disconnect from session so that the updates on updatedOwner are not directly saved in db
        em.detach(updatedOwner);
        updatedOwner
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .birthyear(UPDATED_BIRTHYEAR);

        restOwnerMockMvc.perform(put("/api/owners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOwner)))
            .andExpect(status().isOk());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeUpdate);
        Owner testOwner = ownerList.get(ownerList.size() - 1);
        assertThat(testOwner.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testOwner.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testOwner.getBirthyear()).isEqualTo(UPDATED_BIRTHYEAR);
    }

    @Test
    @Transactional
    public void updateNonExistingOwner() throws Exception {
        int databaseSizeBeforeUpdate = ownerRepository.findAll().size();

        // Create the Owner

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOwnerMockMvc.perform(put("/api/owners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(owner)))
            .andExpect(status().isBadRequest());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOwner() throws Exception {
        // Initialize the database
        ownerService.save(owner);

        int databaseSizeBeforeDelete = ownerRepository.findAll().size();

        // Delete the owner
        restOwnerMockMvc.perform(delete("/api/owners/{id}", owner.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Owner.class);
        Owner owner1 = new Owner();
        owner1.setId(1L);
        Owner owner2 = new Owner();
        owner2.setId(owner1.getId());
        assertThat(owner1).isEqualTo(owner2);
        owner2.setId(2L);
        assertThat(owner1).isNotEqualTo(owner2);
        owner1.setId(null);
        assertThat(owner1).isNotEqualTo(owner2);
    }
}
