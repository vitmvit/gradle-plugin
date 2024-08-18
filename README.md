# Разработка Gradle Plugin

1. Плагин должен добавить задачу в проект
2. В зависимости от того, в какой ветке задача была запущена
    1. Определить последнюю опубликованную версию (последний git tag)
    2. Определить версию текущего билда исходя из следующей логики
        1. dev/qa - инкремент минорной версии
        2. stage - добавить постфикс -rc
        3. master - инкремент мажорной версии
        4. Из любой другой ветки - постфикс -SNAPSHOT
    3. Присвоить соответствующий git tag
    4. Опубликовать его в origin

3. Если текущему состоянию проекта уже присвоен git tag, новый присваиваться не должен
4. Если в рабочей директории есть незакоммиченные изменения, вывести в лог номер сборки с постфиксом .uncommitted, git
   tag при этом не создавать

## Ожидаемый результат

1. Если тегов нет
   При запуске из ветки dev будет опубликован tag v0.1				
   После слияния изменений из dev в qa и запуске из qa v0.2				
   После слияния изменений из qa в stage и запуске из stage v0.3-rc				
   После слияния изменений из stage в master и запуске из master v1.0

2. Если тег v1.0 есть
   При запуске из ветки dev будет опубликован tag v1.1				
   После слияния изменений из dev в qa и запуске из qa v1.2				
   После слияния изменений из qa в stage и запуске из stage v1.3-rc				
   После слияния изменений из stage в master и запуске из master v2.0