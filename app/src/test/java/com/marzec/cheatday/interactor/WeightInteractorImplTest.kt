package com.marzec.cheatday.interactor

//
//class WeightInteractorTest {
//
//    private val userRepository: UserRepository = mock()
//    private val targetRepository: UserPreferencesRepository = mock()
//    private val weightResultRepository: WeightResultRepository = mock()
//    private val daysInteractor: DaysInteractor = mock()
//
//    lateinit var interactor: WeightInteractor
//
//    @BeforeEach
//    fun setUp() {
//        whenever(daysInteractor.incrementCheatDays(any())).thenReturn(Completable.complete())
//        whenever(userRepository.getCurrentUserFlow()).thenReturn(flowOf())
//        whenever(weightResultRepository.observeWeights(any())).thenReturn(flowOf())
//        whenever(targetRepository.observeTargetWeight()).thenReturn(Observable.just(0f))
//        interactor = WeightInteractorImpl(
//            userRepository,
//            targetRepository,
//            weightResultRepository,
//            daysInteractor
//        )
//    }
//
//    @Test
//    fun observeTargetWeight() = runBlockingTest {
//        Assertions.assertEquals(
//            0f,
//            interactor.observeTargetWeight().first()
//        )
//        verify(targetRepository).observeTargetWeight()
//    }
//
//    @Test
//    fun setTargetWeight() {
//        interactor.setTargetWeight(1f)
//        verify(targetRepository).setTargetWeight(1f)
//    }
//
//    @Test
//    fun observeWeights() = runBlockingTest {
//        whenever(userRepository.getCurrentUserFlow()).thenReturn(flowOf(User("user_id", "mail")))
//        whenever(weightResultRepository.observeWeights("user_id")).thenReturn(
//            flowOf(
//                listOf(
//                    stubWeightResult(),
//                    stubWeightResult()
//                )
//            )
//        )
//
//        assertEquals(
//            listOf(
//                stubWeightResult(),
//                stubWeightResult()
//            ),
//            interactor.observeWeights().first()
//        )
//
//        verify(weightResultRepository).observeWeights("user_id")
//    }
//
//    @Test
//    fun addWeight() = runBlockingTest {
//        whenever(userRepository.getCurrentUserSuspend()).thenReturn(User("user_id", "mail"))
//        whenever(weightResultRepository.observeLastWeight("user_id")).thenReturn(flowOf())
//
//        interactor.addWeight(stubWeightResult())
//
//        verify(weightResultRepository).putWeight("user_id", stubWeightResult())
//    }
//
//    @Test
//    fun updateWeight() = runBlockingTest {
//        whenever(userRepository.getCurrentUserSuspend()).thenReturn(User("user_id", "mail"))
//        interactor.updateWeight(stubWeightResult())
//        verify(weightResultRepository).updateWeight("user_id", stubWeightResult())
//    }
//
//    @Test
//    fun `if added weight is not of today, then don't change cheat days count`() = runBlockingTest {
//        whenever(userRepository.getCurrentUserSuspend()).thenReturn(User("user_id", "mail"))
//
//        interactor.addWeight(
//            stubWeightResult(value = 90.5f)
//        )
//
//        verify(weightResultRepository).putWeight("user_id", stubWeightResult(value = 90.5f))
//        verifyNoMoreInteractions(weightResultRepository)
//        verify(daysInteractor, never()).incrementCheatDays(any())
//    }
//
//    @Nested
//    inner class Added_today_weight {
//
//        @BeforeEach
//        fun setUp() {
//            DateTimeUtils.setCurrentMillisFixed(0)
//        }
//
//        @Test
//        fun `if new weight is greater than older and target weight, then decrease cheat day, and additionally decrease cheat day with diff from integers values of weights`() =
//            runBlockingTest {
//                whenever(userRepository.getCurrentUserSuspend()).thenReturn(User("user_id", "mail"))
//                whenever(weightResultRepository.observeLastWeight("user_id")).thenReturn(
//                    flowOf(
//                        stubWeightResult(value = 93.4f)
//                    )
//                )
//                whenever(weightResultRepository.observeMinWeight("user_id")).thenReturn(
//                    flowOf(
//                        stubWeightResult(value = 91f)
//                    )
//                )
//                whenever(targetRepository.observeTargetWeight()).thenReturn(Observable.just(90f))
//
//                interactor.addWeight(
//                    stubWeightResult(value = 95.5f)
//                )
//
//                verify(daysInteractor).incrementCheatDays(-3)
//            }
//
//        @Test
//        fun `if new weight is greater than older with diff smaller than 1 kg than and then target weight, then decrease cheat day`() =
//            runBlockingTest {
//                whenever(userRepository.getCurrentUserSuspend()).thenReturn(User("user_id", "mail"))
//                whenever(weightResultRepository.observeLastWeight("user_id")).thenReturn(
//                    flowOf(
//                        stubWeightResult(value = 93.4f)
//                    )
//                )
//                whenever(targetRepository.observeTargetWeight()).thenReturn(Observable.just(90f))
//                whenever(weightResultRepository.observeMinWeight("user_id")).thenReturn(
//                    flowOf(
//                        stubWeightResult(value = 91f)
//                    )
//                )
//
//                interactor.addWeight(
//                    stubWeightResult(value = 93.6f)
//                )
//
//                verify(daysInteractor).incrementCheatDays(-1)
//            }
//
//        @Test
//        fun `if new weight is greater than older, but not than target weight, then don't change cheat days count`() =
//            runBlockingTest {
//                whenever(userRepository.getCurrentUserSuspend()).thenReturn(User("user_id", "mail"))
//                whenever(weightResultRepository.observeLastWeight("user_id")).thenReturn(
//                    flowOf(
//                        stubWeightResult(value = 93.4f)
//                    )
//                )
//                whenever(targetRepository.observeTargetWeight()).thenReturn(Observable.just(100f))
//                whenever(weightResultRepository.observeMinWeight("user_id")).thenReturn(
//                    flowOf(
//                        stubWeightResult(value = 91f)
//                    )
//                )
//
//                interactor.addWeight(
//                    stubWeightResult(value = 95.5f)
//                )
//
//                verify(daysInteractor, never()).incrementCheatDays(any())
//            }
//
//        @Test
//        fun `if new weight is smaller than older, then increase cheat day, and additionally increase cheat day with diff from integers values of weights`() =
//            runBlockingTest {
//                whenever(userRepository.getCurrentUserSuspend()).thenReturn(User("user_id", "mail"))
//                whenever(weightResultRepository.observeLastWeight("user_id")).thenReturn(
//                    flowOf(
//                        stubWeightResult(value = 93.4f)
//                    )
//                )
//                whenever(targetRepository.observeTargetWeight()).thenReturn(Observable.just(90f))
//                whenever(weightResultRepository.observeMinWeight("user_id")).thenReturn(
//                    flowOf(
//                        stubWeightResult(value = 91f)
//                    )
//                )
//
//                interactor.addWeight(
//                    stubWeightResult(value = 91.5f)
//                )
//
//                verify(daysInteractor).incrementCheatDays(3)
//            }
//
//        @Test
//        fun `if new weight is smaller than older with diff smaller than 1 kg, then increase cheat day`() =
//            runBlockingTest {
//                whenever(userRepository.getCurrentUserSuspend()).thenReturn(User("user_id", "mail"))
//                whenever(weightResultRepository.observeLastWeight("user_id")).thenReturn(
//                    flowOf(
//                        stubWeightResult(value = 93.4f)
//                    )
//                )
//                whenever(targetRepository.observeTargetWeight()).thenReturn(Observable.just(90f))
//                whenever(weightResultRepository.observeMinWeight("user_id")).thenReturn(
//                    flowOf(
//                        stubWeightResult(value = 80.0f)
//                    )
//                )
//
//                interactor.addWeight(
//                    stubWeightResult(value = 93.3f)
//                )
//
//                verify(daysInteractor).incrementCheatDays(1)
//            }
//
//        @Test
//        fun `if new weight is smaller than min weight, then increase cheat day with one extra day`() =
//            runBlockingTest {
//                whenever(userRepository.getCurrentUserSuspend()).thenReturn(User("user_id", "mail"))
//                whenever(weightResultRepository.observeLastWeight("user_id")).thenReturn(
//                    flowOf(
//                        stubWeightResult(value = 91.1f)
//                    )
//                )
//                whenever(targetRepository.observeTargetWeight()).thenReturn(Observable.just(90f))
//                whenever(weightResultRepository.observeMinWeight("user_id")).thenReturn(
//                    flowOf(
//                        stubWeightResult(value = 91f)
//                    )
//                )
//
//                interactor.addWeight(
//                    stubWeightResult(value = 90.5f)
//                )
//
//                verify(daysInteractor).incrementCheatDays(3)
//            }
//
//        @Test
//        fun `if old value not available, then don't cheat days count`() = runBlockingTest {
//            whenever(userRepository.getCurrentUserSuspend()).thenReturn(User("user_id", "mail"))
//            whenever(weightResultRepository.observeLastWeight("user_id")).thenReturn(flowOf(null))
//            whenever(targetRepository.observeTargetWeight()).thenReturn(Observable.just(90f))
//            whenever(weightResultRepository.observeMinWeight("user_id")).thenReturn(flowOf(null))
//
//            interactor.addWeight(
//                stubWeightResult(value = 90.5f)
//            )
//
//            verify(daysInteractor, never()).incrementCheatDays(any())
//        }
//    }
//}