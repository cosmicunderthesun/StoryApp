package com.dicoding.picodiploma.loginwithanimation.view.main

import MainDispatcherRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.dicoding.picodiploma.loginwithanimation.DataDummy
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.remote.respone.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var userRepository: UserRepository

    @Test
    fun `when Get Story Should Not Null and Return Data`() = runTest {
        val userModel =
            UserModel(email = "test@example.com", token = "some-valid-token", isLogin = true)
        Mockito.`when`(userRepository.getSession())
            .thenReturn(flowOf(userModel))

        val dummyQuote = DataDummy.generateDummyQuoteResponse()
        val data: PagingData<ListStoryItem> = StoryPagingSource.snapshot(dummyQuote)
        val expectedQuote = MutableLiveData<PagingData<ListStoryItem>>()
        expectedQuote.postValue(data)

        Mockito.`when`(userRepository.getPagingStory(userModel.token)).thenReturn(expectedQuote)

        val mainViewModel = MainViewModel(userRepository)

        val token = mainViewModel.getSession().getOrAwaitValue().token
        mainViewModel.getPagingStory(token)

        val actualQuote: PagingData<ListStoryItem> = mainViewModel.storyResponse.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualQuote)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyQuote.size, differ.snapshot().size)
        Assert.assertEquals(dummyQuote[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Story Empty Should Return No Data`() = runTest {
        val userModel =
            UserModel(email = "test@example.com", token = "some-valid-token", isLogin = true)
        Mockito.`when`(userRepository.getSession()).thenReturn(flowOf(userModel))

        val data: PagingData<ListStoryItem> = PagingData.from(emptyList())
        val expectedStory = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStory.value = data

        Mockito.`when`(userRepository.getPagingStory(userModel.token)).thenReturn(expectedStory)

        val mainViewModel = MainViewModel(userRepository)

        val token = mainViewModel.getSession().getOrAwaitValue().token

        mainViewModel.getPagingStory(token)

        val actualStory: PagingData<ListStoryItem> = mainViewModel.storyResponse.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        Assert.assertEquals(0, differ.snapshot().size)
    }


    val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

    class StoryPagingSource : PagingSource<Int, ListStoryItem>() {
        companion object {
            fun snapshot(items: List<ListStoryItem>): PagingData<ListStoryItem> {
                return PagingData.from(items)
            }
        }

        override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int {
            return 0
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
            return LoadResult.Page(emptyList(), 0, 1)
        }
    }
}

