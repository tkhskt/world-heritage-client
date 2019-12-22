package com.github.gericass.world_heritage_client.home.category

import com.github.gericass.world_heritage_client.common.applyInstantTaskExecutor
import com.github.gericass.world_heritage_client.common.applyTestDispatcher
import com.github.gericass.world_heritage_client.common.vo.Status
import com.github.gericass.world_heritage_client.data.AvgleRepository
import com.github.gericass.world_heritage_client.data.model.Categories
import com.google.common.truth.Truth
import io.mockk.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object CategoryViewModelSpec : Spek({

    applyTestDispatcher()
    applyInstantTaskExecutor()

    val avgleRepository by memoized {
        mockk<AvgleRepository>()
    }
    val categoryViewModel by memoized {
        spyk(CategoryViewModel(avgleRepository), recordPrivateCalls = true)
    }

    describe("api") {
        it("called") {
            categoryViewModel.init()
            coVerify(exactly = 1) { avgleRepository.getCategories() }
        }
        it("succeeded") {
            val category: Categories.Category = mockk(relaxed = true)
            val resp: Categories.Response = mockk { every { categories } returns listOf(category) }
            coEvery { avgleRepository.getCategories() } returns Categories(resp, true)
            coEvery { categoryViewModel.fetchVideos(any()) } just runs
            categoryViewModel.init()
            Truth.assertThat(categoryViewModel.categories.value?.status).isEqualTo(Status.SUCCESS)
            verify(exactly = 1) { categoryViewModel.fetchVideos(any()) }
        }
        it("failed") {
            coEvery { avgleRepository.getCategories() } throws Exception()
            categoryViewModel.init()
            Truth.assertThat(categoryViewModel.categories.value?.status).isEqualTo(Status.ERROR)
        }
    }
})