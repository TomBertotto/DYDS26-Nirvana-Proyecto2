# Blueprint Plan: MVVC Recipes App (Desktop)

Goal: create a project like this from zero, in small steps that always compile and run.

Guidelines: no code comments while developing, apply clean code practices, follow clean architecture, and adhere to SOLID principles throughout.

Note: names like `Recipe`, `Recipes`, `TheMealDB`, and `Open Food Facts` are recipe-specific in this blueprint.
For another domain (e.g., music), keep the same structure but swap those names/APIs with domain equivalents.

## Step 0 - Create the Gradle project shell
- Create a new Gradle Kotlin project with a root `build.gradle.kts`, `settings.gradle.kts`, `gradle.properties`, and `gradle/` wrapper files.
- Add the `composeApp` module with a `composeApp/build.gradle.kts` configured for Compose Desktop.
- Ensure Kotlin/JVM targets match the desired desktop runtime.

Checkpoint (project should sync/build):
```powershell
./gradlew.bat tasks
```

## Step 1 - Add the basic app entry point
- Create `composeApp/src/desktopMain/kotlin/edu/dyds/recipes/main.kt` with a minimal Compose Desktop window.
- Add a minimal `presentation/App.kt` composable and call it from `main.kt`.

Checkpoint (app should run with a blank window):
```powershell
./gradlew.bat :composeApp:run
```

## Step 2 - Define the domain layer
- Create `domain/entity/Recipe.kt` with the neutral recipe fields used by the UI.
- Create `domain/entity/NutritionInfo.kt` for estimated nutrition data.
- Create `domain/entity/WeeklyPlan.kt` for the saved weekly meal plan.
- Create repository interfaces under `domain/repository`.
- Create use case interfaces/implementations under `domain/usecase`.

Checkpoint (compile only, no UI changes):
```powershell
./gradlew.bat :composeApp:compileKotlinDesktop
```

## Step 3 - Define data layer contracts
- Add data interfaces under `data/external` and `data/local` (e.g., external sources, local data sources).
- Add a local data source contract for weekly plan persistence.
- Add data repository implementation skeletons under `data/repository` that satisfy domain contracts.
- Use simple in-memory or stub results so the app still compiles and runs.

Checkpoint (app still runs, showing stub data):
```powershell
./gradlew.bat :composeApp:run
```

## Step 4 - Add the presentation models and screens
- Create UI state models in `presentation/home/RecipesUiState.kt` and `presentation/detail/RecipeDetailUiState.kt`.
- Create a weekly plan UI state model in `presentation/plan/WeeklyPlanUiState.kt`.
- Create view models in `presentation/home/HomeViewModel.kt`, `presentation/detail/DetailViewModel.kt`, and `presentation/plan/WeeklyPlanViewModel.kt` using the use cases.
- Create `HomeScreen.kt`, `DetailScreen.kt`, `WeeklyPlanScreen.kt`, and shared components in `presentation/utils/CommonComposables.kt`.

Checkpoint (app runs with basic UI and stub data):
```powershell
./gradlew.bat :composeApp:run
```

## Step 5 - Wire navigation
- Add `presentation/Navigation.kt` with `NavHost` and `composable` routes.
- Use `rememberNavController()` and pass callbacks from `HomeScreen` to navigate to `DetailScreen` and `WeeklyPlanScreen`.
- Update `App.kt` to render `Navigation()`.

Checkpoint (navigation works between screens):
```powershell
./gradlew.bat :composeApp:run
```

## Step 6 - Add dependency injection wiring
- Create `di/RecipesDependencyInjector.kt` to provide repositories, external sources, use cases, and view models.
- Replace ad-hoc object creation in UI with the injector.

Checkpoint (app still runs with DI wiring):
```powershell
./gradlew.bat :composeApp:run
```

## Step 7 - Implement local data source
- Implement `RecipesLocalDataSourceImpl.kt` with an in-memory cache.
- Implement `WeeklyPlanLocalDataSourceImpl.kt` with an in-memory plan store.
- Add unit tests in `desktopTest` for the local data sources.

Checkpoint (tests pass):
```powershell
./gradlew.bat :composeApp:desktopTest
```

## Step 8 - Implement external sources (TheMealDB and Open Food Facts)
- Add `data/external/themealdb` and `data/external/openfoodfacts` implementations that map API models into `domain/entity/Recipe` and `domain/entity/NutritionInfo`.
- Keep API calls behind interfaces and return nullable results for missing data.
- Add proxy classes used for tests.

Note: TheMealDB/Open Food Facts are recipe-specific; for another domain, replace these with the two external APIs that serve the same roles.

Checkpoint (compile with external sources wired but using safe placeholders if no keys):
```powershell
./gradlew.bat :composeApp:compileKotlinDesktop
```

## Step 9 - Implement the broker and repository logic
- Implement `RecipeExternalSourceBroker.kt` to merge results from TheMealDB/Open Food Facts and handle nulls.
- Update `RecipesRepositoryImpl.kt` to use the broker for details and TheMealDB for popular lists.
- Ensure repository returns domain `Recipe` and `NutritionInfo` objects only.

Note: the broker remains a dual-source merge layer; the concrete API names change with the domain.

Checkpoint (app runs and details load with available data):
```powershell
./gradlew.bat :composeApp:run
```

## Step 10 - Add tests and fakes for data/external
- Create fakes under `desktopTest/commonFakes`.
- Add broker tests in `RecipeExternalSourceBrokerTest.kt` for nutrition merge and null behavior.
- Add proxy tests under `data/external/proxy`.

Checkpoint (tests pass):
```powershell
./gradlew.bat :composeApp:desktopTest
```

## Step 11 - Add tests for repository, use cases, and view models
- Add repository tests in `RecipesRepositoryImplTest.kt`.
- Add use case tests in `UseCaseTests.kt`.
- Add view model tests in `HomeViewModelTest.kt`, `DetailViewModelTest.kt`, and `WeeklyPlanViewModelTest.kt`.

Checkpoint (tests pass):
```powershell
./gradlew.bat :composeApp:desktopTest
```

## Step 12 - Add UI polish and resources
- Add resources under `composeApp/src/desktopMain/resources` as needed.
- Update `CommonComposables.kt` to reuse UI pieces.
- Ensure UI state handles loading, empty, and error states cleanly.

Checkpoint (app runs with final UI):
```powershell
./gradlew.bat :composeApp:run
```

## Step 13 - Optional documentation
- Create `MVVC_STRUCTURE.md` to capture the module structure and test layout.
- Add a README with run/test commands.

Checkpoint (docs updated, app unchanged):
```powershell
./gradlew.bat :composeApp:compileKotlinDesktop
```
