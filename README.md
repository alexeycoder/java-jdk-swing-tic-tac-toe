## Java Swing: Графический интерфейс на примере игры Крестики-нолики.

### Изменения:

* Логика игры и представление разделены: пакеты [domain](src/domain) и
[gui](src/gui) соответственно.
* Добавлена интерактивность компонентам окна `SettingsWindow`: отображение текущих
значений слайдеров, зависимость диапазона слайдера от значения другого слайдера.
* Обработчики событий компонентов и игры в окне `SettingsWindow` вынесены в отдельные
методы класса окна вместо использования экземпляров анонимных классов слушателей.
* Центрирование главного окна относительно экрана и окна настроек относительно
главного.
* Прочие улучшения...

*Компоновка окна настроек:*

![Компоновка окна настроек 1](https://raw.githubusercontent.com/alexeycoder/illustrations/main/java-jdk-swing-tictactoe/ex-1.png)

![Компоновка окна настроек 1](https://raw.githubusercontent.com/alexeycoder/illustrations/main/java-jdk-swing-tictactoe/ex-2.png)

*Главное окно с игровым полем:*

![Главное окно 1](https://raw.githubusercontent.com/alexeycoder/illustrations/main/java-jdk-swing-tictactoe/ex-3.png)

![Главное окно 2](https://raw.githubusercontent.com/alexeycoder/illustrations/main/java-jdk-swing-tictactoe/ex-4.png)
