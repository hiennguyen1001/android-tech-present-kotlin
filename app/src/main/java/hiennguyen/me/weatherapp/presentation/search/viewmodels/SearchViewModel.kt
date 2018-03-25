package hiennguyen.me.weatherapp.presentation.search.viewmodels

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.AutocompleteFilter.TYPE_FILTER_CITIES
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import hiennguyen.me.weatherapp.common.base.viewmodels.BaseViewModel
import hiennguyen.me.weatherapp.data.models.local.SearchCity
import hiennguyen.me.weatherapp.domain.interactor.SearchCityUseCase
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject


class SearchViewModel @Inject constructor(application: Application, private val geoDataClient: GeoDataClient,
                                          private val searchCityUseCase: SearchCityUseCase) : BaseViewModel(application) {

    val cityListLiveData = MutableLiveData<List<SearchCity>>()

    fun search(key: String) {
        mIsLoading.set(true)
        val results = geoDataClient.getAutocompletePredictions(key, BOUNDS_GREATER_SYDNEY, filter)
        searchCityUseCase.singleSubscribeWith(onSuccess = {
            cityListLiveData.value = it
            mIsLoading.set(false)
        }, onError = {
            mIsLoading.set(false)
        }, params = results)
    }

    override fun onCleared() {
        searchCityUseCase.dispose()
        super.onCleared()
    }

    companion object {
        private val BOUNDS_GREATER_SYDNEY = LatLngBounds(
                LatLng(-34.041458, 150.790100), LatLng(-33.682247, 151.383362))
        val filter = AutocompleteFilter.Builder().setTypeFilter(TYPE_FILTER_CITIES).build()
    }

}