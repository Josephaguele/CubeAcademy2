package com.cube.cubeacademy


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
import com.cube.cubeacademy.lib.di.Repository
import com.cube.cubeacademy.lib.models.Nominee
import com.cube.cubeacademy.viewmodel.NominationViewModel
import io.mockk.MockKAnnotations.init
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()



    private lateinit var viewModel: NominationViewModel
    private lateinit var repo: Repository


    @Before
    fun setup() {
        repo = mock(Repository::class.java)
        viewModel = NominationViewModel(repo, mainCoroutineRule.dispatcher)

    }


    @Test
    fun fetchCubeNominees() = mainCoroutineRule.runBlockingTest {
        val cubeNominees = listOf(
            Nominee(nomineeId = "4204-aa-bb", firstName = "Solomon", lastName = "Olakunle"),
            Nominee(nomineeId = "4205-ac-bcb", firstName = "Teju", lastName = "Emmanuel")
        )
        `when`(repo.getAllNominees()).thenReturn(cubeNominees)
        viewModel.fetchCubeNominees()
        val result = viewModel.nomineeList.getOrAwaitValue  ()
        assertThat(result[0].nomineeId, `is` ("4204-aa-bb"))

    }
}