document.addEventListener('DOMContentLoaded', () => {
    const trigger = document.getElementById('nav-other');
    const menu = document.getElementById('dropdown-other');
    let timer;

    const entities = [
        { name: 'Duration', url: '/durations' },
        { name: 'Group',    url: '/groups' },
        { name: 'Room',     url: '/rooms' },
        { name: 'RoomType', url: '/room-types' },
        { name: 'Subject',  url: '/subjects' }
    ];

    menu.innerHTML = entities.map(e =>
        `<a href="${e.url}" class="dropdown-link">${e.name}</a>`
    ).join('');

    const toggle = (isVisible) => {
        clearTimeout(timer);
        if (isVisible) {
            menu.style.display = 'block';
        } else {
            timer = setTimeout(() => menu.style.display = 'none', 200);
        }
    };

    trigger.addEventListener('mouseenter', () => toggle(true));
    trigger.addEventListener('mouseleave', () => toggle(false));

    menu.addEventListener('mouseenter', () => clearTimeout(timer));
    menu.addEventListener('mouseleave', () => toggle(false));
});