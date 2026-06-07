# MVVC Core Structure

Guidelines: no code comments while developing, apply clean code practices, follow clean architecture, and adhere to SOLID principles throughout.

composeApp/src/desktopMain/kotlin/edu/dyds/recipes/
- main.kt
- data/
  - external/
    - RecipeDetailExternalSource.kt
    - RecipeExternalSourceBroker.kt
    - PopularRecipesExternalSource.kt
    - openfoodfacts/
      - OpenFoodFactsRecipesExternalSource.kt
      - RemoteOpenFoodFacts.kt
    - proxy/
    - themealdb/
  - local/
    - RecipesLocalDataSource.kt
    - RecipesLocalDataSourceImpl.kt
    - WeeklyPlanLocalDataSource.kt
    - WeeklyPlanLocalDataSourceImpl.kt
  - repository/
    - RecipesRepositoryImpl.kt
- di/
  - RecipesDependencyInjector.kt
- domain/
  - entity/
    - Recipe.kt
    - NutritionInfo.kt
    - WeeklyPlan.kt
  - qualifier/
    - RecipeQualifier.kt
  - repository/
    - RecipesRepository.kt
  - usecase/
    - GetRecipeDetailsUseCase.kt
    - GetRecipeDetailsUseCaseImpl.kt
    - GetPopularRecipesUseCase.kt
    - GetPopularRecipesUseCaseImpl.kt
    - GetWeeklyPlanUseCase.kt
    - GetWeeklyPlanUseCaseImpl.kt
    - SaveWeeklyPlanUseCase.kt
    - SaveWeeklyPlanUseCaseImpl.kt
- presentation/
  - App.kt
  - Navigation.kt
  - detail/
    - DetailScreen.kt
    - DetailViewModel.kt
    - RecipeDetailUiState.kt
  - home/
    - HomeScreen.kt
    - HomeViewModel.kt
    - RecipesUiState.kt
  - plan/
    - WeeklyPlanScreen.kt
    - WeeklyPlanViewModel.kt
    - WeeklyPlanUiState.kt
  - utils/
    - CommonComposables.kt

## MVVC Core Structure Notes
- main.kt: Desktop entry point and app bootstrap.
- data: Data layer sources, repositories, and external/local access.
- data/external: Remote sources, brokers, and API-specific adapters.
- data/external/openfoodfacts: Open Food Facts integration and models.
- data/external/themealdb: TheMealDB integration and models.
- data/external/proxy: External source proxies used by tests and broker.
- data/local: Local persistence sources (recipes cache and weekly plan).
- data/repository: Repository implementations bridging data/domain.
- di: Dependency wiring for app modules.
- domain: Domain layer interfaces and business rules.
- domain/entity: Core entities used by the app.
- domain/qualifier: Qualifiers for DI or source selection.
- domain/repository: Repository contracts.
- domain/usecase: Use cases that coordinate domain actions.
- presentation: UI layer and view models.
- presentation/detail: Detail screen UI and state.
- presentation/home: Home screen UI and state.
- presentation/plan: Weekly plan screen UI and state.
- presentation/utils: Shared UI utilities and components.

# Tests

composeApp/src/desktopTest/kotlin/
- TestExample.kt
- edu/dyds/recipes/
  - commonFakes/
    - FakeRecipeDetailExternalSource.kt
    - FakePopularRecipesExternalSource.kt
    - FakeNutritionExternalSource.kt
  - data/
    - external/
      - RecipeExternalSourceBrokerTest.kt
      - proxy/
        - OpenFoodFactsRecipesExternalSourceProxyTest.kt
        - TheMealDBRecipesExternalSourceProxyTest.kt
    - local/
      - RecipesLocalDataTest.kt
      - WeeklyPlanLocalDataTest.kt
    - repository/
      - RecipesRepositoryImplTest.kt
  - domain/
    - usecase/
      - UseCaseTests.kt
  - presentation/
    - detail/
      - DetailViewModelTest.kt
    - home/
      - HomeViewModelTest.kt
    - plan/
      - WeeklyPlanViewModelTest.kt

## Test Modules Notes
- commonFakes: Test doubles for external sources and data providers.
- data/external: Broker and proxy behavior tests.
- data/local: Local data source and weekly plan tests.
- data/repository: Repository integration tests with fakes.
- domain/usecase: Use case behavior tests.
- presentation/detail: Detail view model tests.
- presentation/home: Home view model tests.
- presentation/plan: Weekly plan view model tests.

## Compose Usage Notes
- @Composable functions define UI screens, navigation routes, and shared UI widgets in `presentation` (e.g., `Navigation.kt`, `HomeScreen.kt`, `DetailScreen.kt`, `WeeklyPlanScreen.kt`, `CommonComposables.kt`).
- Navigation uses `NavHost` and `composable` routes to wire screens, passing view models and callbacks for navigation and user actions.
- Screen composables render state from view models and delegate reusable UI pieces to shared composables in `presentation/utils`.
